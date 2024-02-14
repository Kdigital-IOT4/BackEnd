package com.baro.domain.order.repository.DAO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderWaitingListDAO {
    private List<OrderReadDAO> orderReadList;
}
