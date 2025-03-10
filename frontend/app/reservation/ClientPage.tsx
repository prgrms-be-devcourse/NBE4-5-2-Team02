"use client";

import { Calendar, momentLocalizer } from "react-big-calendar";
import moment, { duration } from "moment";
import "moment/locale/ko";
import "react-big-calendar/lib/css/react-big-calendar.css";
import "./CustomCalendar.css";
import { useEffect, useState } from "react";
import DateBox from "./DataBox";
import { useRouter } from "next/navigation";

interface SlotInfo {
  start: Date;
  end: Date;
  slots: Date[];
  action: "click" | "doubleClick" | "select";
}

export default function ClientPage({
  me,
  post,
}: {
  me: {
    id: number;
    nickname: string;
    username: string;
    profileImage: string;
    email: string;
    phoneNumber: string;
    address: {
      mainAddress: string;
      detailAddress: string;
      zipcode: string;
    };
    createdAt: string;
    score: number;
    credit: number;
  };
  post: {
    userId: any;
    id: number;
    title: string;
    content: string;
    createdAt: string;
    updatedAt: string;
    category: string;
    priceType: string;
    price: number;
    latitude: number;
    longitude: number;
    viewCount: number;
  };
}) {
  const [date, setDate] = useState<Date>(new Date());
  const [selectedDates, setSelectedDates] = useState<Date[]>([]);
  const [startTime, setStartTime] = useState<string>("00:00");
  const [endTime, setEndTime] = useState<string>("00:00");
  const [showTimeForm, setShowTimeForm] = useState<boolean>(false);
  const [dateRange, setDateRange] = useState<Date[]>([]);
  const [totalPrice, setTotalPrice] = useState<number>(0);
  const [usageDuration, setUsageDuration] = useState<string>("");
  const [reservedDates, setReservedDates] = useState<Date[]>([]);
  const [events, setEvents] = useState<any[]>([]);

  useEffect(() => {
    calculateTotalPrice(dateRange);
  }, [startTime, endTime, dateRange]);

  useEffect(() => {
    const loadReservedEvents = async () => {
      const events = await fetchReservedEvents(post.id);
      setEvents(events);
      console.log("Reserved events:", events);
      processReservedEvents(events);
    };
    loadReservedEvents();
  }, [me.id, post.id]);

  // 예약된 날짜 가져오기
  const fetchReservedEvents = async (postId: number) => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/v1/reservations/reservatedDates/${postId}`
      );
      if (response.ok) {
        const data = await response.json();
        return data.data.map((reservation: any) => ({
          title: `예약중`,
          start: moment(reservation.startTime).toDate(),
          end: moment(reservation.endTime).toDate(),
          allDay: false,
        }));
      } else {
        console.error("Failed to fetch reserved events");
        return [];
      }
    } catch (error) {
      console.error("Error fetching reserved events:", error);
      return [];
    }
  };

  const processReservedEvents = (events: any[]) => {
    const dates: Date[] = [];
    events.forEach((event) => {
      let currentDate = moment(event.start).clone();
      const endDate = moment(event.end).clone();

      while (currentDate.isSameOrBefore(endDate, "day")) {
        dates.push(currentDate.clone().toDate());
        currentDate.add(1, "day");
      }
    });
    setReservedDates(dates);
  };

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

  // 캘린더 날짜 범위 계산
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
      calculateTotalPrice(range); // 총 가격 계산
    } else {
      setDateRange([]);
      setTotalPrice(0);
    }
  };

  // 총 가격 계산
  const calculateTotalPrice = (range: Date[]) => {
    if (range.length > 0) {
      const duration = range.length;
      const price = post.price;
      const priceType = post.priceType;

      if (priceType === "HOUR") {
        const startDateTime = moment(selectedDates[0])
          .startOf("day")
          .add(moment(startTime, "HH:mm").hours(), "hours")
          .add(moment(startTime, "HH:mm").minutes(), "minutes");
        const endDateTime = moment(selectedDates[1])
          .startOf("day")
          .add(moment(endTime, "HH:mm").hours(), "hours")
          .add(moment(endTime, "HH:mm").minutes(), "minutes");
        const diff = moment.duration(endDateTime.diff(startDateTime));
        const hours = Math.ceil(diff.asHours());

        setUsageDuration(`${hours}시간 이용`);
        setTotalPrice(price * hours);
      } else if (priceType === "DAY") {
        setTotalPrice(price * duration);
        setUsageDuration(`${duration}일 이용`);
      }
    } else {
      setTotalPrice(0);
      setUsageDuration("");
    }
  };

  // 캘린더 슬롯 선택 - 이미 예약된 날짜는 선택 불가능
  const handleSelectSlot = ({ start, end }: SlotInfo) => {
    const correctedEnd = moment(end).subtract(1, "day").toDate();
    const range: Date[] = [];
    let current = moment(start).clone();

    while (current.isSameOrBefore(moment(correctedEnd), "day")) {
      if (
        reservedDates.some((reservedDate) =>
          current.isSame(moment(reservedDate), "day")
        ) &&
        !events.some(
          (event) =>
            current.isSame(moment(event.start), "day") ||
            current.isSame(moment(event.end), "day")
        )
      ) {
        alert("선택하신 날짜는 이미 예약되어 있거나 예약된 기간을 포함합니다.");
        return;
      }
      range.push(current.clone().toDate());
      current.add(1, "day");
    }

    setSelectedDates([start, correctedEnd]);
    calculateDateRange([start, correctedEnd]);
    setShowTimeForm(true);
  };

  const isDateInEventRange = (date: Date, event: any) => {
    const startDate = moment(event.start).startOf("day");
    const endDate = moment(event.end).startOf("day");
    const targetDate = moment(date).startOf("day");
    return (
      targetDate.isSameOrAfter(startDate) && targetDate.isSameOrBefore(endDate)
    );
  };

  // 캘린더 선택 날짜 스타일
  const dayPropGetter = (date: Date) => {
    if (dateRange.some((rangeDate) => moment(rangeDate).isSame(date, "day"))) {
      return {
        style: {
          backgroundColor: "lightblue",
        },
      };
    }
    // 예약 시작일 또는 종료일인 경우 노란색으로 표시 (먼저 검사)
    if (
      events.some((event) => moment(event.start).isSame(moment(date), "day"))
    ) {
      return {
        style: {
          backgroundColor: "Pink",
        },
      };
    }
    if (events.some((event) => moment(event.end).isSame(moment(date), "day"))) {
      return {
        style: {
          backgroundColor: "Pink",
        },
      };
    }
    if (events.some((event) => isDateInEventRange(date, event))) {
      return {
        style: {
          backgroundColor: "Lightcoral",
          pointerEvents: "none",
        },
      };
    }
    if (
      reservedDates.some((reservedDate) =>
        moment(reservedDate).isSame(date, "day")
      ) &&
      !events.some(
        (event) =>
          moment(event.start).isSame(date, "day") ||
          moment(event.end).isSame(date, "day")
      )
    ) {
      return {
        style: {
          backgroundColor: "Lightcoral",
          pointerEvents: "none",
        },
      };
    }
    return {};
  };
  // 시간 변경 핸들러
  const handleStartTimeChange = (time: string) => {
    setStartTime(time);
  };

  const handleEndTimeChange = (time: string) => {
    setEndTime(time);
  };
  const router = useRouter();
  // 보증금
  const deposit = 10000;

  const handleReservation = async () => {
    try {
      if (usageDuration.startsWith("-")) {
        alert("시간 선택이 잘못되었습니다.");
        return;
      }
      if (selectedDates.length === 2) {
        const startDate = moment(selectedDates[0])
          .format("YYYY-MM-DD")
          .concat(`T${startTime}:00`);
        const endDate = moment(selectedDates[1])
          .format("YYYY-MM-DD")
          .concat(`T${endTime}:00`);

        const reservationData = {
          postId: post.id,
          renterId: me.id,
          ownerId: post.userId,
          startTime: startDate,
          endTime: endDate,
          deposit: deposit,
          rentalFee: totalPrice,
        };

        const response = await fetch(
          "http://localhost:8080/api/v1/reservations/request",
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(reservationData),
          }
        );

        if (response.ok) {
          const reservation = await response.json();
          console.log("예약 신청 성공:", reservation);
          alert("예약 신청이 완료되었습니다.");

          router.push(
            `/reservation/complete?reservationId=${reservation.data.id}`
          );
        } else {
          const errorData = await response.json();
          console.error("예약 신청 실패:", errorData);
          alert(
            `예약 신청에 실패했습니다. ${errorData.message || "서버 오류"}`
          );
        }
      } else {
        alert("날짜를 선택해주세요.");
      }
    } catch (error) {
      console.error("예약 신청 중 오류 발생:", error);
      alert("예약 신청 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="flex flex-col items-center justify-center">
      <div className="mt-4 flex justify-center w-full">
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
          events={events} // 이벤트 데이터 전달
        />
      </div>
      <div className="flex flex-col mt-10 space-x-4 justify-center">
        {" "}
        {/* 시작일, 종료일 데이터 박스 */}
        <div className="w-full mb-6">
          <DateBox
            date={selectedDates[0]}
            onTimeChange={handleStartTimeChange}
            time={startTime}
            events={events}
          />
        </div>
        <div className="w-full">
          <DateBox
            date={selectedDates[1]}
            onTimeChange={handleEndTimeChange}
            time={endTime}
            events={events}
          />
        </div>
      </div>
      <div className="flex flex-col justify-center items-center mt-10 w-[50%] text-xl">
        <div className="flex flex-col mt-4 w-full">
          <span>✅ 예약 시 확인해주세요!</span>
          <b className="mt-6">간단한 당부 멘트</b>
          <span>
            당일 혹은 1일 전 예약으로 확정되지 않을 수 있으니 2~3일 전에
            예약해주세요.
          </span>
        </div>
        {/* 기간 및 합계 폼 */}
        <div className="w-full">
          <div className="mt-10">
            {moment(selectedDates[0]).format("M월 DD일")} {startTime} ~{" "}
            {moment(selectedDates[1]).format("M월 DD일")} {endTime}
          </div>
          <div className="mt-1">{usageDuration}</div>
          <div className="mt-4">
            <b>대여료</b> {totalPrice}원
          </div>{" "}
          <div className="mt-1">
            {" "}
            <b>보증금</b> {deposit}원
          </div>
          <hr />
          <div className="mt-4 font-bold">합계 {totalPrice + deposit}원</div>
        </div>
        <button
          className="mt-4 mb-4 bg-green-300 text-black p-2 rounded-md w-full font-bold"
          onClick={handleReservation}
        >
          동의하고 예약 신청하기
        </button>
      </div>
    </div>
  );
}
