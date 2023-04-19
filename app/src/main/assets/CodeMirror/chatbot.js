const Chatbot = {
    "!name": "chatbot",
    "replier": {
      "!type": "fn()",
      "reply": {
        "!type": "fn(msg: string) -> bool",
        "!doc": "채팅이 수신된 채팅방으로 응답을 전송합니다."
      },
      "replyDelayed": {
        "!type": "fn(msg: string, ms: number) -> bool",
        "!doc": "ms 밀리초 뒤에 해당 채팅방으로 응답을 전송합니다."
      },
      "markAsRead": {
        "!type": "fn() -> bool",
        "!doc": "해당 채팅방에 응답을 보내지 않고 읽음으로 처리합니다."
      }
    },
    // "someOtherGlobal": "string"
  }