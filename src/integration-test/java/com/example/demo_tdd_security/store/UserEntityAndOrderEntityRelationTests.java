package com.example.demo_tdd_security.store;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.store.UserEntity;
import com.example.demo_tdd_security.authentication.store.UserJpaRepository;
import com.example.demo_tdd_security.item.store.ItemJpaRepository;
import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.order.store.OrderEntity;
import com.example.demo_tdd_security.order.store.OrderJpaRepository;
import com.example.demo_tdd_security.shipping.store.ShippingAddressJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserEntityAndOrderEntityRelationTests {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private ItemJpaRepository itemJpaRepository;

    @Autowired
    private ShippingAddressJpaRepository shippingAddressJpaRepository;


    private String userId = null;

    @BeforeEach
    void setUp() {
        UserEntity userEntity = new UserEntity(User.builder().name("John").build());

        UserEntity savedUserEntity = userJpaRepository.save(userEntity);
        userId = savedUserEntity.getId();

        List<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.add(new OrderEntity(Order.builder().user(userEntity.toDomain()).price(100).build()));
        orderEntities.add(new OrderEntity(Order.builder().user(userEntity.toDomain()).price(200).build()));

        orderJpaRepository.saveAll(orderEntities);

        em.flush();
        em.clear();
    }

    @Test
    void tdd_for_savedOrderAndUser_referencesEachOrder() throws Exception{
        System.out.println("orderJpaRepository = " + orderJpaRepository.findAll().size());
        System.out.println("userId = " + userId);
        assertThat(orderJpaRepository.findAll().get(0).getUserEntity().getId()).isEqualTo(userId);
        assertThat(userJpaRepository.findById(userId).get().getOrders().size()).isEqualTo(2);
    }

    @Test
    void tdd_for_deletingUser_deletesOrders() throws Exception{
        // given
        userJpaRepository.deleteAll();
        // then
        assertThat(orderJpaRepository.findAll().size()).isEqualTo(0);
    }






}
