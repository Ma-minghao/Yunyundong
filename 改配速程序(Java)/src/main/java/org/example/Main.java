package org.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.example.pojo.JsonResponse;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 创建 JsonResponse 类的实例
        JsonResponse jsonResponse = new JsonResponse();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入 tasklist.json 文件地址:");
        String jsonString = scanner.nextLine();
        try (FileReader reader = new FileReader(jsonString)) {
            StringBuilder content = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                content.append((char) i);
            }
            // 使用 Fastjson 解析 JSON 字符串
            JSONObject jsonObject = JSON.parseObject(content.toString());
            // 将解析后的 JSON 对象转换为 JsonResponse 类实例
            jsonResponse.setMsg(jsonObject.getString("msg"));
            jsonResponse.setCode(jsonObject.getIntValue("code"));
            // 递归解析 data 对象
            JSONObject jsonData = jsonObject.getJSONObject("data");
            JsonResponse.Data data = new JsonResponse.Data();
            data.setRecordMileage(jsonData.getDoubleValue("recordMileage"));
            data.setRecodePace(jsonData.getDoubleValue("recodePace"));
            data.setRecodeCadence(jsonData.getIntValue("recodeCadence"));
            data.setRecodeDislikes(jsonData.getIntValue("recodeDislikes"));
            data.setDuration(jsonData.getIntValue("duration"));
            data.setSchoolId(jsonData.getIntValue("schoolId"));
            // 解析 pointsList 数组
            List<JsonResponse.Data.Point> pointsList = jsonData.getJSONArray("pointsList").toJavaList(JsonResponse.Data.Point.class);
            data.setPointsList(pointsList);
            // 解析 manageList 数组
            List<JsonResponse.Data.Manage> manageList = jsonData.getJSONArray("manageList").toJavaList(JsonResponse.Data.Manage.class);
            data.setManageList(manageList);
            // 设置 data 对象到 JsonResponse 实例
            jsonResponse.setData(data);
            // 打印解析后的 JsonResponse 对象
//            System.out.println(jsonResponse);
            System.out.println("tasklist.json解析成功！");
            System.out.println("当前配速为" + jsonResponse.getData().getRecodePace());
            System.out.println("当前距离为" + jsonResponse.getData().getRecordMileage());
            System.out.println("请输入配速和时间的倍率：");
            double rate = scanner.nextDouble();
            if (rate > 0) {
                multiplyValues(jsonResponse, rate);
            } else {
                throw new IllegalArgumentException("n must be a positive double");
            }

            System.out.println("修改成功");
            // 将修改后的 JsonResponse 对象转换为 JSON 字符串，并写入 tasklist.json 文件
            try (FileWriter writer = new FileWriter("输出的tasklist.json")) {
                JSON.writeJSONString(writer, jsonResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close(); // 关闭 Scanner 对象
        }
    }

    private static void multiplyValues(JsonResponse response, double rate) {

        // 递归解析 data 对象
        JSONObject jsonData = JSON.parseObject(JSON.toJSONString(response.getData()));
        JsonResponse.Data data = JSON.parseObject(jsonData.toJSONString(), new TypeReference<JsonResponse.Data>() {
        });

        // 遍历 pointsList 并乘以 rate
        for (JsonResponse.Data.Point point : data.getPointsList()) {
            point.setRunTime((int) (point.getRunTime() * rate));
            point.setSpeed(point.getSpeed() * rate);
        }

        // 乘以 rate 的倍数
        data.setDuration((int) (data.getDuration() * rate));
        data.setRecodePace(data.getRecodePace() * rate);

        // 更新 data 对象到 JsonResponse 实例
        response.setData(data);

        return;
    }
}
