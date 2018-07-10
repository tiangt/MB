package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;

public class ExpJson {
    public class AnchorExpItem{
        int bjExpValue;
        String expName;
        String expType;
        int sjExpvalue;
        int sjNeedExpValue;
        int totalExpValue;

        public int getBjExpValue() {
            return bjExpValue;
        }

        public String getExpName() {
            return expName;
        }

        public String getExpType() {
            return expType;
        }

        public int getSjExpvalue() {
            return sjExpvalue;
        }

        public int getSjNeedExpValue() {
            return sjNeedExpValue;
        }

        public int getTotalExpValue() {
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
        int userId;

        public List<ExpLevelItem> getLevels() {
            return levels;
        }

        public int getUserId() {
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
