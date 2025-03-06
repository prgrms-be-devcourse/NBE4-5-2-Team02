"use client";

import { Calendar, momentLocalizer } from "react-big-calendar";
import moment from "moment";
import "moment/locale/ko";
import "react-big-calendar/lib/css/react-big-calendar.css";
import "./CustomCalendar.css";
import { useState } from "react";
import DateBox from "./DataBox";

interface SlotInfo {
  start: Date;
  end: Date;
  slots: Date[];
  action: "click" | "doubleClick" | "select";
}

export default function ClientPage() {
  const [date, setDate] = useState<Date>(new Date());
  const [selectedDates, setSelectedDates] = useState<Date[]>([]);
  const [startTime, setStartTime] = useState<string>("00:00");
  const [endTime, setEndTime] = useState<string>("00:00");
  const [showTimeForm, setShowTimeForm] = useState<boolean>(false);
  const [dateRange, setDateRange] = useState<Date[]>([]);

  const handleNavigate = (newDate: Date) => {
    setDate(newDate);
  };

  const formats = {
    dateFormat: "D",
    dayFormat: (date: Date, culture: any, localizer: any) =>
      localizer.format(date, "dddd", culture),
    weekdayFormat: (date: Date, culture: any, localizer: any) =>
      localizer.format(date, "ddd", culture),
    monthHeaderFormat: (date: Date, culture: any, localizer: any) =>
      localizer.format(date, "YYYY년 MM월", culture),
  };

  const localizer = momentLocalizer(moment);

  const messages = {
    today: "오늘",
    previous: "이전",
    next: "다음",
    month: "월",
    week: "주",
    day: "일",
    agenda: "일정",
    date: "날짜",
    time: "시간",
    event: "이벤트",
  };

  const calculateDateRange = (dates: Date[]) => {
    if (dates.length === 2) {
      const start = moment(dates[0]).startOf("day");
      const end = moment(dates[1]).startOf("day");
      const range: Date[] = [];
      let current = moment(start);

      while (current.isSameOrBefore(end, "day")) {
        range.push(current.clone().toDate());
        current.add(1, "day");
      }
      setDateRange(range);
    } else {
      setDateRange([]);
    }
  };

  const handleSelectSlot = ({ start, end }: SlotInfo) => {
    const correctedEnd = moment(end).subtract(1, "day").toDate();
    setSelectedDates([start, correctedEnd]);
    calculateDateRange([start, correctedEnd]);
    setShowTimeForm(true);
  };

  const handleReservation = () => {
    if (selectedDates.length === 2) {
      const startDate = moment(selectedDates[0])
        .format("YYYY-MM-DD")
        .concat(`T${startTime}`);
      const endDate = moment(selectedDates[1])
        .format("YYYY-MM-DD")
        .concat(`T${endTime}`);

      console.log("startDate:", startDate);
      console.log("endDate:", endDate);

      setShowTimeForm(false);
    }
  };

  const dayPropGetter = (date: Date) => {
    if (dateRange.some((rangeDate) => moment(rangeDate).isSame(date, "day"))) {
      return {
        style: {
          backgroundColor: "lightblue",
        },
      };
    }
    return {};
  };

  const handleStartTimeChange = (time: string) => {
    setStartTime(time);
  };

  const handleEndTimeChange = (time: string) => {
    setEndTime(time);
  };

  return (
    <div>
      <div className="mt-4 flex justify-center">
        <Calendar
          localizer={localizer}
          startAccessor="start"
          endAccessor="end"
          style={{ height: 800 }}
          date={date}
          onNavigate={handleNavigate}
          views={{ month: true }}
          messages={messages}
          formats={formats}
          onSelectSlot={handleSelectSlot}
          selectable
          dayPropGetter={dayPropGetter}
        />
      </div>
      <div className="flex mt-4 space-x-4 justify-center">
        {" "}
        {/* space-x-4 클래스 추가 */}
        <DateBox
          date={selectedDates[0]}
          onTimeChange={handleStartTimeChange}
          time={startTime}
        />
        <DateBox
          date={selectedDates[1]}
          onTimeChange={handleEndTimeChange}
          time={endTime}
        />
      </div>
    </div>
  );
}
