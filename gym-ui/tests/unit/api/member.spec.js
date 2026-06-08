jest.mock("@/utils/request", () => jest.fn((config) => Promise.resolve(config)));

const request = require("@/utils/request");
const {
  listMember,
  getMember,
  applyCard,
  addMember,
  updateMember,
  delMember,
} = require("@/api/gym/member");

describe("member api", () => {
  beforeEach(() => {
    request.mockClear();
  });

  test("listMember should call list endpoint", async () => {
    await listMember({ pageNum: 1, pageSize: 10 });
    expect(request).toHaveBeenCalledWith({
      url: "/gym/member/list",
      method: "get",
      params: { pageNum: 1, pageSize: 10 },
    });
  });

  test("getMember should call detail endpoint", async () => {
    await getMember(1001);
    expect(request).toHaveBeenCalledWith({
      url: "/gym/member/1001",
      method: "get",
    });
  });

  test("addMember should call post endpoint", async () => {
    const data = { name: "alice" };
    await addMember(data);
    expect(request).toHaveBeenCalledWith({
      url: "/gym/member",
      method: "post",
      data,
    });
  });

  test("updateMember should call put endpoint", async () => {
    const data = { memberId: 1, name: "bob" };
    await updateMember(data);
    expect(request).toHaveBeenCalledWith({
      url: "/gym/member",
      method: "put",
      data,
    });
  });

  test("delMember should call delete endpoint", async () => {
    await delMember(5);
    expect(request).toHaveBeenCalledWith({
      url: "/gym/member/5",
      method: "delete",
    });
  });

  test("applyCard should call apply endpoint", async () => {
    await applyCard(1, 12);
    expect(request).toHaveBeenCalledWith({
      url: "/gym/member/apply/1/12",
      method: "get",
    });
  });
});
