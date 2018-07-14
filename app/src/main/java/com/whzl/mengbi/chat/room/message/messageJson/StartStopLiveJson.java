package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;

public class StartStopLiveJson {
    private String eventCode; //开播：start_live,  停播: stop_live
    private ContextEntity context;

    public String getEventCode() {
        return eventCode;
    }

    public ContextEntity getContext() {
        return context;
    }

    public class ContextEntity {
        List<ShowStreams> show_streams;
        String platform_type;
        int programId;
        int width;
        int height;

        public List<ShowStreams> getShow_streams() {
            return show_streams;
        }

        public String getPlatform_type() {
            return platform_type;
        }

        public int getProgramId() {
            return programId;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public class ShowStreams {
        public String getStreamType() {
            return streamType;
        }

        public String getStreamAddress() {
            return streamAddress;
        }

        private String streamType;
        private String streamAddress;
    }
}
