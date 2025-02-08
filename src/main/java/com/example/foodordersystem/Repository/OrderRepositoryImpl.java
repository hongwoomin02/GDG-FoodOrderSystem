package com.example.foodordersystem.Repository;

import com.example.foodordersystem.Entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class OrderRepositoryImpl implements CustomOrderRepository{
    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Order> findAllOrders(){
        QOrder order = QOrder.order;
        QUser user = QUser.user;
        QOrderItem orderItem = QOrderItem.orderItem;
        QFood food = QFood.food;

        return queryFactory
                .selectFrom(order)
                .leftJoin(order.user, user).fetchJoin()
                .leftJoin(order.orderItems, orderItem).fetchJoin()
                .leftJoin(orderItem.food, food).fetchJoin()
                .fetch();
    }
}
