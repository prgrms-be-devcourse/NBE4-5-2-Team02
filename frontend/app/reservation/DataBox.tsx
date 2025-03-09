import moment from "moment";

interface DateBoxProps {
  date: Date | null;
  onTimeChange: (time: string) => void;
  time: string;
  events: any[];
}

const DateBox: React.FC<DateBoxProps> = ({
  date,
  onTimeChange,
  time,
  events,
}) => {
  if (!date) {
    return (
      <div className="p-4 border rounded-md w-64 h-32 flex items-center justify-center">
        날짜를 선택하세요.
      </div>
    );
  }

  const timeBlocks = Array.from({ length: 48 }, (_, i) => {
    const hour = Math.floor(i / 2);
    const minute = i % 2 === 0 ? "00" : "30";
    return `${hour.toString().padStart(2, "0")}:${minute}`;
  });

  const isTimeReserved = (timeBlock: string) => {
    if (!events) return false;
    const dateString = moment(date).format("YYYY-MM-DD");
    const timeBlockDateTime = moment(`${dateString}T${timeBlock}`);

    return events.some((event) => {
      const startDate = moment(event.start).startOf("day");
      const endDate = moment(event.end).startOf("day");
      const currentDate = moment(date).startOf("day");

      if (
        currentDate.isSameOrAfter(startDate) &&
        currentDate.isSameOrBefore(endDate)
      ) {
        const startTime = moment(event.start);
        const endTime = moment(event.end);

        if (
          currentDate.isSame(startDate) &&
          timeBlockDateTime.isBefore(startTime)
        ) {
          return false;
        }

        if (
          currentDate.isSame(endDate) &&
          timeBlockDateTime.isSameOrAfter(endTime)
        ) {
          return false;
        }

        return true;
      }
      return false;
    });
  };

  const handleTimeBlockClick = (timeBlock: string) => {
    if (!isTimeReserved(timeBlock)) {
      onTimeChange(timeBlock);
    } else {
      alert("선택하신 시간은 이미 예약되었습니다.");
    }
  };

  return (
    <div className="p-4 border rounded-md flex flex-col items-center">
      <div>{moment(date).format("YYYY-MM-DD")}</div>
      <div className="mt-2">{time}</div>
      <div className="flex flex-col mt-2">
        <div className="flex">
          {timeBlocks.slice(0, 24).map((timeBlock) => (
            <button
              key={timeBlock}
              className={`p-1 border rounded-sm text-xs whitespace-nowrap ${
                isTimeReserved(timeBlock)
                  ? "bg-gray-200 text-gray-500"
                  : "hover:bg-gray-100"
              }`}
              onClick={() => handleTimeBlockClick(timeBlock)}
            >
              {timeBlock}
            </button>
          ))}
        </div>
        <div className="flex">
          {timeBlocks.slice(24).map((timeBlock) => (
            <button
              key={timeBlock}
              className={`p-1 border rounded-sm text-xs whitespace-nowrap ${
                isTimeReserved(timeBlock)
                  ? "bg-gray-200 text-gray-500"
                  : "hover:bg-gray-100"
              }`}
              onClick={() => handleTimeBlockClick(timeBlock)}
            >
              {timeBlock}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default DateBox;
