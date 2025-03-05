import moment from "moment";
import { useState } from "react";

interface DateBoxProps {
  date: Date | null;
  onTimeChange: (time: string) => void;
  time: string;
}

const DateBox: React.FC<DateBoxProps> = ({ date, onTimeChange, time }) => {
  if (!date) {
    return <div>날짜를 선택하세요.</div>;
  }

  return (
    <div>
      <div>{moment(date).format("YYYY-MM-DD")}</div>
      <input
        type="time"
        value={time}
        onChange={(e) => onTimeChange(e.target.value)}
      />
    </div>
  );
};

export default DateBox;
