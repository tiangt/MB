package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * author: yaobo wu
 * date:   On 2018/8/3
 */
public class UpdateProgramJson {
    String eventCode;
    ContextEntity context;

    public ContextEntity getContext() {
        return context;
    }

    public class ContextEntity{
        ProgramEntity program;

        public ProgramEntity getProgram() {
            return program;
        }
    }

    public class ProgramEntity {
        public String getProgramName() {
            return programName;
        }

        public String getPubNotice() {
            return pubNotice;
        }

        public String getSubscriptionNum() {
            return subscriptionNum;
        }

        String programName;
        String pubNotice;
        String subscriptionNum;  //关注数
    }
}
