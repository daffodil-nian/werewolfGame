package org.arrinna.nobugdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 启动游戏的请求
 * 只需要包括有几位玩家以及每位玩家的配置就行
 *
 * 除此之外还得满足10个玩家一起玩
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartGameDto {
    private List<GamePlayerConfigDao> players;

    /** 不传 players 时由服务端自动生成 10 人；传了则必须恰好 10 人 */
    public boolean isValid(){
        if (players == null || players.isEmpty()) {
            return true;
        }
        return players.size() == 10;
    }

}
