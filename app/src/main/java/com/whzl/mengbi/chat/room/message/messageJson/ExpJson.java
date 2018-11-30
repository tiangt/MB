package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;

public class ExpJson {
    public class AnchorExpItem{
        long bjExpValue;
        String expName;
        String expType;
        long sjExpvalue;
        long sjNeedExpValue;
        long totalExpValue;

        public long getBjExpValue() {
            return bjExpValue;
        }

        public String getExpName() {
            return expName;
        }

        public String getExpType() {
            return expType;
        }

        public long getSjExpvalue() {
            return sjExpvalue;
        }

        public long getSjNeedExpValue() {
            return sjNeedExpValue;
        }

        public long getTotalExpValue() {
            return totalExpValue;
        }
    }

    public class ExpLevelItem{
        String levelType;
        String levelName;
        String levelPic;
        List<AnchorExpItem> expList;

        public String getLevelType() {
            return levelType;
        }

        public String getLevelName() {
            return levelName;
        }

        public String getLevelPic() {
            return levelPic;
        }

        public List<AnchorExpItem> getExpList() {
            return expList;
        }
    }

    public class ExpContext {
        List<ExpLevelItem> levels;
        long userId;

        public List<ExpLevelItem> getLevels() {
            return levels;
        }

        public long getUserId() {
            return userId;
        }
    }
    String eventCode;
    ExpContext context;

    public String getEventCode() {
        return eventCode;
    }

    public ExpContext getContext() {
        return context;
    }
}
