package org.example.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class JsonResponse {
    private String msg;
    private int code;
    private Data data;

    @lombok.Data
    public static class Data {
        private double recordMileage;
        private double recodePace;
        private int recodeCadence;
        private int recodeDislikes;
        private int duration;
        private List<Point> pointsList;
        private int schoolId;
        private List<Manage> manageList;

        @lombok.Data
        public static class Point {
            private int id;
            private String point;
            private double speed;
            private int runStatus;
            private int runRecordId;
            private int runTime;
            private String isFence;
            private int runStep;
            private double runMileage;
        }

        @lombok.Data
        public static class Manage {
            private String point;
            private String marked;
            private int index;
        }
    }
}
