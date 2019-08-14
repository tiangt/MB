package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author nobody
 * @date 2019-08-14
 */
public class PkQualifyingBean implements Parcelable {

    /**
     * rankAnchorInfo : {"nextRankNeedValue":28,"haveValue":272,"currentRank":2,"rankPkTime":18,"victoryTime":4,"victoryRatio":0.2222222222222222,"continueVictoryTime":0,"rankName":"萌新III","rankId":3,"bestTeam":[{"userId":30000139,"lastUpdateTime":"2019-06-03 19:46:13","nickname":"小猪佩奇就是我啊","contributeScore":1370600},{"userId":30000809,"lastUpdateTime":"2019-06-20 10:24:07","nickname":"Youiil","contributeScore":823600},{"userId":30000154,"lastUpdateTime":"2019-04-25 10:11:43","nickname":"揉一揉","contributeScore":334400}]}
     * seasonInfo : {"seasonLeftSec":1513530,"seasonName":"S8"}
     * lastestPkRecord : [{"programId":100163,"programName":"萌友30000803","status":"T","roomUserCount":0,"cover":"http://localtest-img.mengbitv.com/default/000/00/05/86_400x400.jpg","anchorId":30000803,"anchorNickname":"huhuujkhhh","anchorLevelName":"皇冠24","anchorLevelValue":39,"anchorAvatar":"http://localtest-img.mengbitv.com/avatar/030/00/08/03_100x100.jpg?1565753669","showStreamData":{"height":540,"width":960,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100163","flv":"https://livedown.mengbitv.com/live/100163.flv","hls":"http://livedown.mengbitv.com/live/100163/playlist.m3u8"},"lastShowBeginTime":"2019-07-25 09:36:27","isPk":"F","isWeekStar":"F","isPopularity":"F","recordInfo":{"nickname":"huhuujkhhh","userId":30000803,"lastUpdateTime":"2019-04-26 09:47:31","anchorLevel":39,"mvpUserInfo":null,"result":"T","date":"2019-08-08 19:50:52","score":5}},{"programId":100163,"programName":"萌友30000803","status":"T","roomUserCount":0,"cover":"http://localtest-img.mengbitv.com/default/000/00/05/86_400x400.jpg","anchorId":30000803,"anchorNickname":"huhuujkhhh","anchorLevelName":"皇冠24","anchorLevelValue":39,"anchorAvatar":"http://localtest-img.mengbitv.com/avatar/030/00/08/03_100x100.jpg?1565753669","showStreamData":{"height":540,"width":960,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100163","flv":"https://livedown.mengbitv.com/live/100163.flv","hls":"http://livedown.mengbitv.com/live/100163/playlist.m3u8"},"lastShowBeginTime":"2019-07-25 09:36:27","isPk":"F","isWeekStar":"F","isPopularity":"F","recordInfo":{"nickname":"huhuujkhhh","userId":30000803,"lastUpdateTime":"2019-04-26 09:47:31","anchorLevel":39,"mvpUserInfo":{"nickname":"小猪佩奇就是我啊","userId":30000139,"lastUpdateTime":"2019-06-03 19:46:13","score":null},"result":"V","date":"2019-08-08 19:26:25","score":72}},{"programId":100163,"programName":"萌友30000803","status":"T","roomUserCount":0,"cover":"http://localtest-img.mengbitv.com/default/000/00/05/86_400x400.jpg","anchorId":30000803,"anchorNickname":"huhuujkhhh","anchorLevelName":"皇冠24","anchorLevelValue":39,"anchorAvatar":"http://localtest-img.mengbitv.com/avatar/030/00/08/03_100x100.jpg?1565753669","showStreamData":{"height":540,"width":960,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100163","flv":"https://livedown.mengbitv.com/live/100163.flv","hls":"http://livedown.mengbitv.com/live/100163/playlist.m3u8"},"lastShowBeginTime":"2019-07-25 09:36:27","isPk":"F","isWeekStar":"F","isPopularity":"F","recordInfo":{"nickname":"huhuujkhhh","userId":30000803,"lastUpdateTime":"2019-04-26 09:47:31","anchorLevel":39,"mvpUserInfo":{"nickname":"小猪佩奇就是我啊","userId":30000139,"lastUpdateTime":"2019-06-03 19:46:13","score":null},"result":"V","date":"2019-08-08 19:22:38","score":70}},{"programId":100163,"programName":"萌友30000803","status":"T","roomUserCount":0,"cover":"http://localtest-img.mengbitv.com/default/000/00/05/86_400x400.jpg","anchorId":30000803,"anchorNickname":"huhuujkhhh","anchorLevelName":"皇冠24","anchorLevelValue":39,"anchorAvatar":"http://localtest-img.mengbitv.com/avatar/030/00/08/03_100x100.jpg?1565753669","showStreamData":{"height":540,"width":960,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100163","flv":"https://livedown.mengbitv.com/live/100163.flv","hls":"http://livedown.mengbitv.com/live/100163/playlist.m3u8"},"lastShowBeginTime":"2019-07-25 09:36:27","isPk":"F","isWeekStar":"F","isPopularity":"F","recordInfo":{"nickname":"huhuujkhhh","userId":30000803,"lastUpdateTime":"2019-04-26 09:47:31","anchorLevel":39,"mvpUserInfo":{"nickname":"小猪佩奇就是我啊","userId":30000139,"lastUpdateTime":"2019-06-03 19:46:13","score":null},"result":"V","date":"2019-08-08 19:14:33","score":70}},{"programId":100163,"programName":"萌友30000803","status":"T","roomUserCount":0,"cover":"http://localtest-img.mengbitv.com/default/000/00/05/86_400x400.jpg","anchorId":30000803,"anchorNickname":"huhuujkhhh","anchorLevelName":"皇冠24","anchorLevelValue":39,"anchorAvatar":"http://localtest-img.mengbitv.com/avatar/030/00/08/03_100x100.jpg?1565753669","showStreamData":{"height":540,"width":960,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100163","flv":"https://livedown.mengbitv.com/live/100163.flv","hls":"http://livedown.mengbitv.com/live/100163/playlist.m3u8"},"lastShowBeginTime":"2019-07-25 09:36:27","isPk":"F","isWeekStar":"F","isPopularity":"F","recordInfo":{"nickname":"huhuujkhhh","userId":30000803,"lastUpdateTime":"2019-04-26 09:47:31","anchorLevel":39,"mvpUserInfo":null,"result":"T","date":"2019-08-08 19:11:34","score":5}}]
     */

    public RankAnchorInfoBean rankAnchorInfo;
    public SeasonInfoBean seasonInfo;
    public List<LastestPkRecordBean> lastestPkRecord;

    public static class RankAnchorInfoBean implements Parcelable {
        /**
         * nextRankNeedValue : 28
         * haveValue : 272
         * currentRank : 2
         * rankPkTime : 18
         * victoryTime : 4
         * victoryRatio : 0.2222222222222222
         * continueVictoryTime : 0
         * rankName : 萌新III
         * rankId : 3
         * bestTeam : [{"userId":30000139,"lastUpdateTime":"2019-06-03 19:46:13","nickname":"小猪佩奇就是我啊","contributeScore":1370600},{"userId":30000809,"lastUpdateTime":"2019-06-20 10:24:07","nickname":"Youiil","contributeScore":823600},{"userId":30000154,"lastUpdateTime":"2019-04-25 10:11:43","nickname":"揉一揉","contributeScore":334400}]
         */

        public int nextRankNeedValue;
        public int haveValue;
        public int currentRank;
        public int rankPkTime;
        public int victoryTime;
        public double victoryRatio;
        public int continueVictoryTime;
        public String rankName;
        public int rankId;
        public List<BestTeamBean> bestTeam;

        public static class BestTeamBean implements Parcelable {
            /**
             * userId : 30000139
             * lastUpdateTime : 2019-06-03 19:46:13
             * nickname : 小猪佩奇就是我啊
             * contributeScore : 1370600
             */

            public int userId;
            public String lastUpdateTime;
            public String nickname;
            public int contributeScore;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.userId);
                dest.writeString(this.lastUpdateTime);
                dest.writeString(this.nickname);
                dest.writeInt(this.contributeScore);
            }

            public BestTeamBean() {
            }

            protected BestTeamBean(Parcel in) {
                this.userId = in.readInt();
                this.lastUpdateTime = in.readString();
                this.nickname = in.readString();
                this.contributeScore = in.readInt();
            }

            public static final Parcelable.Creator<BestTeamBean> CREATOR = new Parcelable.Creator<BestTeamBean>() {
                @Override
                public BestTeamBean createFromParcel(Parcel source) {
                    return new BestTeamBean(source);
                }

                @Override
                public BestTeamBean[] newArray(int size) {
                    return new BestTeamBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.nextRankNeedValue);
            dest.writeInt(this.haveValue);
            dest.writeInt(this.currentRank);
            dest.writeInt(this.rankPkTime);
            dest.writeInt(this.victoryTime);
            dest.writeDouble(this.victoryRatio);
            dest.writeInt(this.continueVictoryTime);
            dest.writeString(this.rankName);
            dest.writeInt(this.rankId);
            dest.writeTypedList(this.bestTeam);
        }

        public RankAnchorInfoBean() {
        }

        protected RankAnchorInfoBean(Parcel in) {
            this.nextRankNeedValue = in.readInt();
            this.haveValue = in.readInt();
            this.currentRank = in.readInt();
            this.rankPkTime = in.readInt();
            this.victoryTime = in.readInt();
            this.victoryRatio = in.readDouble();
            this.continueVictoryTime = in.readInt();
            this.rankName = in.readString();
            this.rankId = in.readInt();
            this.bestTeam = in.createTypedArrayList(BestTeamBean.CREATOR);
        }

        public static final Parcelable.Creator<RankAnchorInfoBean> CREATOR = new Parcelable.Creator<RankAnchorInfoBean>() {
            @Override
            public RankAnchorInfoBean createFromParcel(Parcel source) {
                return new RankAnchorInfoBean(source);
            }

            @Override
            public RankAnchorInfoBean[] newArray(int size) {
                return new RankAnchorInfoBean[size];
            }
        };
    }

    public static class SeasonInfoBean implements Parcelable {
        /**
         * seasonLeftSec : 1513530
         * seasonName : S8
         */

        public int seasonLeftSec;
        public String seasonName;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.seasonLeftSec);
            dest.writeString(this.seasonName);
        }

        public SeasonInfoBean() {
        }

        protected SeasonInfoBean(Parcel in) {
            this.seasonLeftSec = in.readInt();
            this.seasonName = in.readString();
        }

        public static final Parcelable.Creator<SeasonInfoBean> CREATOR = new Parcelable.Creator<SeasonInfoBean>() {
            @Override
            public SeasonInfoBean createFromParcel(Parcel source) {
                return new SeasonInfoBean(source);
            }

            @Override
            public SeasonInfoBean[] newArray(int size) {
                return new SeasonInfoBean[size];
            }
        };
    }

    public static class LastestPkRecordBean implements Parcelable {
        /**
         * programId : 100163
         * programName : 萌友30000803
         * status : T
         * roomUserCount : 0
         * cover : http://localtest-img.mengbitv.com/default/000/00/05/86_400x400.jpg
         * anchorId : 30000803
         * anchorNickname : huhuujkhhh
         * anchorLevelName : 皇冠24
         * anchorLevelValue : 39
         * anchorAvatar : http://localtest-img.mengbitv.com/avatar/030/00/08/03_100x100.jpg?1565753669
         * showStreamData : {"height":540,"width":960,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100163","flv":"https://livedown.mengbitv.com/live/100163.flv","hls":"http://livedown.mengbitv.com/live/100163/playlist.m3u8"}
         * lastShowBeginTime : 2019-07-25 09:36:27
         * isPk : F
         * isWeekStar : F
         * isPopularity : F
         * recordInfo : {"nickname":"huhuujkhhh","userId":30000803,"lastUpdateTime":"2019-04-26 09:47:31","anchorLevel":39,"mvpUserInfo":null,"result":"T","date":"2019-08-08 19:50:52","score":5}
         */

        public int programId;
        public String programName;
        public String status;
        public int roomUserCount;
        public String cover;
        public int anchorId;
        public String anchorNickname;
        public String anchorLevelName;
        public int anchorLevelValue;
        public String anchorAvatar;
        public ShowStreamDataBean showStreamData;
        public String lastShowBeginTime;
        public String isPk;
        public String isWeekStar;
        public String isPopularity;
        public RecordInfoBean recordInfo;
        /**
         * nickname : 小猪佩奇就是我啊
         * userId : 30000139
         * lastUpdateTime : 2019-06-03 19:46:13
         * score : null
         */

        public String nickname;
        public int userId;
        public String lastUpdateTime;
        public long score;

        public static class ShowStreamDataBean implements Parcelable {
            /**
             * height : 540
             * width : 960
             * liveType : PC
             * rtmp : rtmp://livedown.mengbitv.com/live/100163
             * flv : https://livedown.mengbitv.com/live/100163.flv
             * hls : http://livedown.mengbitv.com/live/100163/playlist.m3u8
             */

            public int height;
            public int width;
            public String liveType;
            public String rtmp;
            public String flv;
            public String hls;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.height);
                dest.writeInt(this.width);
                dest.writeString(this.liveType);
                dest.writeString(this.rtmp);
                dest.writeString(this.flv);
                dest.writeString(this.hls);
            }

            public ShowStreamDataBean() {
            }

            protected ShowStreamDataBean(Parcel in) {
                this.height = in.readInt();
                this.width = in.readInt();
                this.liveType = in.readString();
                this.rtmp = in.readString();
                this.flv = in.readString();
                this.hls = in.readString();
            }

            public static final Parcelable.Creator<ShowStreamDataBean> CREATOR = new Parcelable.Creator<ShowStreamDataBean>() {
                @Override
                public ShowStreamDataBean createFromParcel(Parcel source) {
                    return new ShowStreamDataBean(source);
                }

                @Override
                public ShowStreamDataBean[] newArray(int size) {
                    return new ShowStreamDataBean[size];
                }
            };
        }

        public static class RecordInfoBean implements Parcelable {
            /**
             * nickname : huhuujkhhh
             * userId : 30000803
             * lastUpdateTime : 2019-04-26 09:47:31
             * anchorLevel : 39
             * mvpUserInfo : null
             * result : T
             * date : 2019-08-08 19:50:52
             * score : 5
             */

            public String nickname;
            public int userId;
            public String lastUpdateTime;
            public int anchorLevel;
            public MvpUserInfoBean mvpUserInfo;
            public String result;
            public String date;
            public int score;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.nickname);
                dest.writeInt(this.userId);
                dest.writeString(this.lastUpdateTime);
                dest.writeInt(this.anchorLevel);
                dest.writeParcelable(this.mvpUserInfo, flags);
                dest.writeString(this.result);
                dest.writeString(this.date);
                dest.writeInt(this.score);
            }

            public RecordInfoBean() {
            }

            protected RecordInfoBean(Parcel in) {
                this.nickname = in.readString();
                this.userId = in.readInt();
                this.lastUpdateTime = in.readString();
                this.anchorLevel = in.readInt();
                this.mvpUserInfo = in.readParcelable(MvpUserInfoBean.class.getClassLoader());
                this.result = in.readString();
                this.date = in.readString();
                this.score = in.readInt();
            }

            public static final Parcelable.Creator<RecordInfoBean> CREATOR = new Parcelable.Creator<RecordInfoBean>() {
                @Override
                public RecordInfoBean createFromParcel(Parcel source) {
                    return new RecordInfoBean(source);
                }

                @Override
                public RecordInfoBean[] newArray(int size) {
                    return new RecordInfoBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.programId);
            dest.writeString(this.programName);
            dest.writeString(this.status);
            dest.writeInt(this.roomUserCount);
            dest.writeString(this.cover);
            dest.writeInt(this.anchorId);
            dest.writeString(this.anchorNickname);
            dest.writeString(this.anchorLevelName);
            dest.writeInt(this.anchorLevelValue);
            dest.writeString(this.anchorAvatar);
            dest.writeParcelable(this.showStreamData, flags);
            dest.writeString(this.lastShowBeginTime);
            dest.writeString(this.isPk);
            dest.writeString(this.isWeekStar);
            dest.writeString(this.isPopularity);
            dest.writeParcelable(this.recordInfo, flags);
            dest.writeString(this.nickname);
            dest.writeInt(this.userId);
            dest.writeString(this.lastUpdateTime);
            dest.writeLong(this.score);
        }

        public LastestPkRecordBean() {
        }

        protected LastestPkRecordBean(Parcel in) {
            this.programId = in.readInt();
            this.programName = in.readString();
            this.status = in.readString();
            this.roomUserCount = in.readInt();
            this.cover = in.readString();
            this.anchorId = in.readInt();
            this.anchorNickname = in.readString();
            this.anchorLevelName = in.readString();
            this.anchorLevelValue = in.readInt();
            this.anchorAvatar = in.readString();
            this.showStreamData = in.readParcelable(ShowStreamDataBean.class.getClassLoader());
            this.lastShowBeginTime = in.readString();
            this.isPk = in.readString();
            this.isWeekStar = in.readString();
            this.isPopularity = in.readString();
            this.recordInfo = in.readParcelable(RecordInfoBean.class.getClassLoader());
            this.nickname = in.readString();
            this.userId = in.readInt();
            this.lastUpdateTime = in.readString();
            this.score = in.readLong();
        }

        public static final Parcelable.Creator<LastestPkRecordBean> CREATOR = new Parcelable.Creator<LastestPkRecordBean>() {
            @Override
            public LastestPkRecordBean createFromParcel(Parcel source) {
                return new LastestPkRecordBean(source);
            }

            @Override
            public LastestPkRecordBean[] newArray(int size) {
                return new LastestPkRecordBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.rankAnchorInfo, flags);
        dest.writeParcelable(this.seasonInfo, flags);
        dest.writeTypedList(this.lastestPkRecord);
    }

    public PkQualifyingBean() {
    }

    protected PkQualifyingBean(Parcel in) {
        this.rankAnchorInfo = in.readParcelable(RankAnchorInfoBean.class.getClassLoader());
        this.seasonInfo = in.readParcelable(SeasonInfoBean.class.getClassLoader());
        this.lastestPkRecord = in.createTypedArrayList(LastestPkRecordBean.CREATOR);
    }

    public static final Parcelable.Creator<PkQualifyingBean> CREATOR = new Parcelable.Creator<PkQualifyingBean>() {
        @Override
        public PkQualifyingBean createFromParcel(Parcel source) {
            return new PkQualifyingBean(source);
        }

        @Override
        public PkQualifyingBean[] newArray(int size) {
            return new PkQualifyingBean[size];
        }
    };
}
