// app/reservations/complete/page.jsx
"use client";

function formatDate(dateTimeString: string | number | Date) {
  const date = new Date(dateTimeString);
  const options = {
    month: "numeric",
    day: "numeric",
    weekday: "short",
    hour: "numeric",
    minute: "2-digit",
    hour12: true,
  };
  return date
    .toLocaleString("ko-KR", options)
    .replace(/(\d+)\.\s(\d+)\s\((.)\)\s(.+)/, "$2. $1 ($3) $4");
}

export default function ClientPage({
  reservation,
  deposit,
}: {
  reservation: {
    id: number;
    status: string;
    postId: number;
    startTime: string;
    endTime: string;
    amount: number;
  };
  deposit: {
    id: number;
    status: string;
    amount: number;
    reutrnReason: string;
  };
}) {
  const formattedStartTime = formatDate(reservation.startTime);
  const formattedEndTime = formatDate(reservation.endTime);

  return (
    <div className="flex justify-center items-center min-h-screen">
      <div className="flex flex-col items-center justify-center w-[70%] h-150 border rounded-lg shadow-md p-6 bg-gray-100">
        <p className="text-5xl font-bold text-blue-600 mb-4">🔍</p>
        <p className="text-4xl font-bold text-green-600 mb-4">
          예약을 확인 중입니다
        </p>
        <p className="text-lg mb-2">예약이 확정되면 알림으로 알려드릴게요!</p>
        {/* 제품명 추가 */}
        <p className="text-lg mb-2 font-bold">제품명 {reservation.postId}</p>
        <p className="text-lg mb-2">
          {formattedStartTime} ~ {formattedEndTime}
        </p>
        <p className="text-lg mb-8">
          대여료 {reservation.amount - deposit.amount}₩ + 보증금{" "}
          {deposit.amount}₩
        </p>
        <p className="text-lg mb-2 font-bold">합계 {reservation.amount}₩</p>
      </div>
    </div>
  );
}
