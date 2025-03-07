// app/reservations/complete/page.jsx
"use client";

interface Reservation {
  id: number;
  status: string;
  postId: number;
  startTime: string;
  endTime: string;
  amount: number;
  rejectionReason: string;
  ownerId: number;
  renterId: number;
}

interface Deposit {
  id: number;
  status: string;
  amount: number;
  returnReason: string;
}

interface me {
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
}

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

function RequestedStatus({
  reservation,
  deposit,
}: {
  reservation: Reservation;
  deposit: Deposit;
}) {
  return (
    <div className="flex flex-col items-center justify-center w-[70%] h-150 border rounded-lg shadow-md p-6 bg-gray-100">
      <p className="text-5xl font-bold text-blue-600 mb-4">🔍</p>
      <p className="text-4xl font-bold text-green-600 mb-4">
        예약을 확인 중입니다
      </p>
      <p className="text-lg mb-2">예약이 확정되면 알림으로 알려드릴게요!</p>
      <p className="text-lg mb-2 font-bold">제품명 {reservation.postId}</p>
      <p className="text-lg mb-2">
        {formatDate(reservation.startTime)} ~ {formatDate(reservation.endTime)}
      </p>
      <p className="text-lg mb-8">
        대여료 {reservation.amount - deposit.amount}₩ + 보증금 {deposit.amount}₩
      </p>
      <p className="text-lg mb-2 font-bold">합계 {reservation.amount}₩</p>
    </div>
  );
}

function ApprovedStatus({
  reservation,
  deposit,
}: {
  reservation: Reservation;
  deposit: Deposit;
}) {
  return (
    <div className="flex flex-col items-center justify-center w-[70%] h-150 border rounded-lg shadow-md p-6 bg-green-200">
      <p className="text-5xl font-bold text-green-600 mb-4">✅</p>
      <p className="text-4xl font-bold text-green-600 mb-4">
        예약이 확정되었습니다!
      </p>
      <p className="text-lg mb-2">즐거운 이용 되세요!</p>
      <p className="text-lg mb-2 font-bold">제품명 {reservation.postId}</p>
      <p className="text-lg mb-2">
        {formatDate(reservation.startTime)} ~ {formatDate(reservation.endTime)}
      </p>
      <p className="text-lg mb-8">
        대여료 {reservation.amount - deposit.amount}₩ + 보증금 {deposit.amount}₩
      </p>
      <p className="text-lg mb-2 font-bold">합계 {reservation.amount}₩</p>
    </div>
  );
}

function InProgressStatus({
  reservation,
  deposit,
}: {
  reservation: Reservation;
  deposit: Deposit;
}) {
  return (
    <div className="flex flex-col items-center justify-center w-[70%] h-150 border rounded-lg shadow-md p-6 bg-yellow-200">
      <p className="text-5xl font-bold text-yellow-600 mb-4">⏳</p>
      <p className="text-4xl font-bold text-yellow-600 mb-4">이용 중입니다</p>
      <p className="text-lg mb-2">이용이 완료되면 반납해주세요!</p>
      <p className="text-lg mb-2 font-bold">제품명 {reservation.postId}</p>
      <p className="text-lg mb-2">
        {formatDate(reservation.startTime)} ~ {formatDate(reservation.endTime)}
      </p>
      <p className="text-lg mb-8">
        대여료 {reservation.amount - deposit.amount}₩ + 보증금 {deposit.amount}₩
      </p>
      <p className="text-lg mb-2 font-bold">합계 {reservation.amount}₩</p>
    </div>
  );
}

function RejectedStatus({
  reservation,
  deposit,
}: {
  reservation: Reservation;
  deposit: Deposit;
}) {
  return (
    <div className="flex flex-col items-center justify-center w-[70%] h-150 border rounded-lg shadow-md p-6 bg-red-200">
      <p className="text-5xl font-bold text-red-600 mb-4">❌</p>
      <p className="text-4xl font-bold text-red-600 mb-4">
        예약이 거절되었습니다
      </p>
      <p className="text-lg mb-2">다른 예약을 시도해주세요.</p>
      <p className="text-lg mb-2 font-bold">사유</p>
      <p className="text-lg mb-2 font-bold">{reservation.rejectionReason}</p>
      <p className="text-lg mb-2">
        {formatDate(reservation.startTime)} ~ {formatDate(reservation.endTime)}
      </p>
      <p className="text-lg mb-8">
        대여료 {reservation.amount - deposit.amount}₩ + 보증금 {deposit.amount}₩
      </p>
      <p className="text-lg mb-2 font-bold">합계 {reservation.amount}₩</p>
      <button className="text-lg bg-red-500 text-white px-4 py-2 rounded-lg">
        다시 예약하기
      </button>
    </div>
  );
}

function DoneStatus({
  reservation,
  deposit,
}: {
  reservation: Reservation;
  deposit: Deposit;
}) {
  return (
    <div className="flex flex-col items-center justify-center w-[70%] h-150 border rounded-lg shadow-md p-6 bg-gray-200">
      <p className="text-5xl font-bold text-gray-600 mb-4">🏁</p>
      <p className="text-4xl font-bold text-gray-600 mb-4">
        이용이 완료되었습니다
      </p>
      <p className="text-lg mb-2">이용해주셔서 감사합니다.</p>
      <p className="text-lg mb-2 font-bold">제품명 {reservation.postId}</p>
      <p className="text-lg mb-2">
        {formatDate(reservation.startTime)} ~ {formatDate(reservation.endTime)}
      </p>
      <p className="text-lg mb-8">
        대여료 {reservation.amount - deposit.amount}₩ + 보증금 {deposit.amount}₩
      </p>
      <p className="text-lg mb-2 font-bold">합계 {reservation.amount}₩</p>
    </div>
  );
}

export default function ClientPage({
  reservation,
  deposit,
  me,
}: {
  reservation: {
    id: number;
    status: string;
    postId: number;
    startTime: string;
    endTime: string;
    amount: number;
    rejectionReason: string;
    ownerId: number;
    renterId: number;
  };
  deposit: {
    id: number;
    status: string;
    amount: number;
    returnReason: string;
  };
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
}) {
  const formattedStartTime = formatDate(reservation.startTime);
  const formattedEndTime = formatDate(reservation.endTime);

  return (
    <div className="flex justify-center items-center min-h-screen">
      {reservation.status === "REQUESTED" && (
        <RequestedStatus reservation={reservation} deposit={deposit} />
      )}
      {reservation.status === "APPROVED" && (
        <ApprovedStatus reservation={reservation} deposit={deposit} />
      )}
      {reservation.status === "IN_PROGRESS" && (
        <InProgressStatus reservation={reservation} deposit={deposit} />
      )}
      {reservation.status === "REJECTED" && (
        <RejectedStatus reservation={reservation} deposit={deposit} />
      )}
      {reservation.status === "DONE" && (
        <DoneStatus reservation={reservation} deposit={deposit} />
      )}
    </div>
  );
}
