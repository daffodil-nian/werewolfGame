package org.arrinna.nobugdemo.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerVo {
    private Integer id;
    private String label;
    private String roleName;
    private String role;
    private boolean alive;
    private boolean goodGuys;
}
