import { fetchWithAuth } from "@/app/lib/util/fetchWithAuth";
import ClientPage from "./ClientPage";

const mockUpOwner = {
  result: "200-1",
  data: {
    id: 1,
    nickname: "Owner",
    username: "testId",
    profileImage: "image.png",
    email: "test@gmail.com",
    phoneNumber: "000-0000-0000",
    address: {
      mainAddress: "서울시 00구 00동",
      detailAddress: "00아파트 101동 1호",
      zipcode: "12345",
    },
    createdAt: "2025-03-04T12:14:00+09:00",
    score: 80,
    credit: 10000,
  },
};

const mockUpRenter = {
  result: "200-1",
  data: {
    id: 2,
    nickname: "Owner",
    username: "testId",
    profileImage: "image.png",
    email: "test@gmail.com",
    phoneNumber: "000-0000-0000",
    address: {
      mainAddress: "서울시 00구 00동",
      detailAddress: "00아파트 202동 2호",
      zipcode: "12345",
    },
    createdAt: "2025-03-04T12:14:00+09:00",
    score: 80,
    credit: 10000,
  },
};

export default async function Page({
  params,
}: {
  params: {
    id: number;
  };
}) {
  const { id } = await params;

  return <ClientPage rid={id} />;
}
