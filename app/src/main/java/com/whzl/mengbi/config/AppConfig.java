package com.whzl.mengbi.config;

/**
 * @author nobody
 * @date 2018/12/18
 */
public interface AppConfig {
    int NUM_TOTAL_GIFT_DIALOG = 8;
    int NUM_SPAN_GIFT_DIALOG = 4;
    int MIN_VALUE_GIFT_DIALOG = 50000;
    int MAX_HEAP_SIZE = 256;

    int ANCHOR_NOT_IN_BLACK_ROOM = -1262;

    String USER_SEND_REDPACKET = "USER_SEND_REDPACKET";
    String OFFICIAL_SEND_REDPACKET = "OFFICIAL_SEND_REDPACKET";
    String PROGRAM_TREASURE_SEND_REDPACKET = "PROGRAM_TREASURE_SEND_REDPACKET";
    String OPEN_REDPACKET = "OPEN_REDPACKET";
    String LUCK_ROB = "LUCK_ROB";
    String GUESS = "GUESS";
    String CARDGAME = "cardGame";
}
