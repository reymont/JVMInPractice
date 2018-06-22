package geym.zbase.ch7.program;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.netbeans.lib.profiler.heap.HeapFactory;
import org.netbeans.modules.profiler.oql.engine.api.OQLEngine;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;


/**
 * https://pan.baidu.com/s/1IGG61cyRCVyOR6E7Pst9xg
 * http://pan.baidu.com/s/1hqJz6hY
 * https://blog.csdn.net/zhangzehai2234/article/details/51338338
 */
public class AveLoadTomcatOOM {
    public static final String dumpFilePath = "C:\\workspace\\java\\java\\tomcat.hprof";

    public static void main(String args[]) throws Exception {
        OQLEngine engine;
        final List<Long> creationTimes = new ArrayList<>(10000);
        engine = new OQLEngine(HeapFactory.createHeap(new File(dumpFilePath)));
        String query = "select s.creationTime from org.apache.catalina.session.StandardSession s";
        engine.executeQuery(query, new OQLEngine.ObjectVisitor() {
            public boolean visit(Object obj) {
                // 查询出来结果为1.403324654125E12。通过BigDecimal将科学计数法转换为普通字符串
                creationTimes.add(Long.valueOf(new BigDecimal((Double) obj).toPlainString()));
                return false;
            }
        });

        Collections.sort(creationTimes);

        double min = creationTimes.get(0) / 1000;
        double max = creationTimes.get(creationTimes.size() - 1) / 1000;
        System.out.println("平均压力：" + creationTimes.size() * 1.0 / (max - min) + "次/秒");
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(creationTimes.get(0));
        start.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        // System.out.println(start.getTime());
        // System.out.println(DateFormatUtils.format(start, "yyyyMMddHHmmss"));

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(creationTimes.get(creationTimes.size() - 1));
        end.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        long startLong = creationTimes.get(0);
        long endLong = creationTimes.get(creationTimes.size() - 1);

        // 通过给定的固定的时间段，输出在该时间段内固定间隔的每个时刻的List集合，如果最后的时间间隔不够的话，将结束时间作为最后的时刻。
        List<Long> list = new ArrayList<>();
        while (startLong <= endLong) {
            list.add(startLong);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startLong);
            calendar.add(Calendar.MILLISECOND, 1000);//1000毫秒
            if (calendar.getTime().getTime() > endLong) {
                if (!start.equals(end)) {
                    list.add(endLong);
                }
                startLong = calendar.getTime().getTime();
            } else {
                startLong = calendar.getTime().getTime();
            }
        }
        // System.out.println(list);

        // 根据区间统计
        Map<String, Integer> map = new HashMap<>();
        // 只循环到31，list.get(31) ~ list.get(32)为最后的范围
        for (int i = 0; i < list.size() - 1; i++) {
            int count = 0;
            // System.out.println(i + ":" + DateFormatUtils.format(list.get(i), "yyyy-MM-dd HH:mm:ss:SSS"));
            // 会抛异常，数组越界
            // System.out.println(i+":"+DateFormatUtils.format(list.get(i+1),"yyyy-MM-dd HH:mm:ss:SSS"));
            for (int j = 0; j < creationTimes.size(); j++) {
                if (creationTimes.get(j) >= list.get(i) && creationTimes.get(j) < list.get(i + 1)) {
                    map.put(DateFormatUtils.format(list.get(i), "yyyy-MM-dd HH:mm:ss:SSS") + "~"
                            + DateFormatUtils.format(list.get(i + 1), "yyyy-MM-dd HH:mm:ss:SSS"), ++count);
                }
            }
        }
        // System.out.println(map);

        // 从大到小排序
        List<Map.Entry<String, Integer>> list2 = new ArrayList<>();
        list2.addAll(map.entrySet());
        Collections.sort(list2, (o1, o2) -> o2.getValue() - o1.getValue());
        list2.forEach(entry -> {
            System.out.println("key:" + entry.getKey() + ",value:" + entry.getValue());
        });
    }
}
