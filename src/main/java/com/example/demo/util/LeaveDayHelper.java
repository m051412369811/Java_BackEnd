package com.example.demo.util;

import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class LeaveDayHelper {

    // 使用 Set 來儲存假日，查詢效能最好
    private static final Set<LocalDate> publicHolidays;
    private static final Set<LocalDate> extraWorkDays;

    // 使用靜態區塊，在類別載入時就將假日資料準備好
    static {
        // 國定假日與彈性放假 (不用上班)
        publicHolidays = Stream.of(
                "2025-01-01", // 元旦
                "2025-01-27", // 春節調整放假
                "2025-01-28", // 除夕
                "2025-01-29", // 春節初一
                "2025-01-30", // 春節初二
                "2025-01-31", // 春節初三
                "2025-02-28", // 228 和平紀念日
                "2025-04-03", // 兒童節補假
                "2025-04-04", // 兒童節/清明節
                "2025-05-01", // 勞動節
                "2025-05-30", // 端午節補假 (端午節在週六)
                "2025-09-29", // 教師節補假 (教師節在週日)
                "2025-10-06", // 中秋節
                "2025-10-10", // 國慶日
                "2025-10-24", // 光復節補假 (光復節在週六)
                "2025-12-25" // 行憲紀念日
        ).map(LocalDate::parse).collect(Collectors.toSet());

        // 週末的補班日 (需要上班)
        extraWorkDays = Stream.of(
                "2025-02-08" // 春節補班
        ).map(LocalDate::parse).collect(Collectors.toSet());
    }

    // 計算兩個日期之間的實際工作日天數。

    public int calculateLeaveDays(final LocalDate startDate, final LocalDate endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return 0;
        }

        int leaveDays = 0;
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            // 獲取當天的星期
            DayOfWeek dayOfWeek = current.getDayOfWeek();
            boolean isWeekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
            boolean isHoliday = publicHolidays.contains(current);
            boolean isExtraWorkDay = extraWorkDays.contains(current);

            // 判斷是否為工作日
            if (isExtraWorkDay) {
                // 1. 如果是補班日，一定是工作日
                leaveDays++;
            } else if (!isWeekend && !isHoliday) {
                // 2. 如果不是週末，也不是國定假日，則是工作日
                leaveDays++;
            }
            // 3. 其他情況（週末、國定假日）則不計入工作日

            // 推進到下一天
            current = current.plusDays(1);
        }

        return leaveDays;
    }
}