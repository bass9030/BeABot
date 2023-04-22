const Replier = {
    "!name": "Replier",
    "Replier": {
      "!type": "fn()",
      "prototype": {
        "reply": {
            "!type": "fn(msg: string) -> bool|fn(room: string, msg: string) -> bool|fn(room: string, msg: string, packageName: string) -> bool",
            "!doc": "채팅이 수신된 채팅방으로 응답을 전송합니다."
          },
          "replyDelayed": {
            "!type": "fn(msg: string, ms: number) -> bool|fn(room: string, msg: string, ms: number) -> bool",
            "!doc": "ms 밀리초 뒤에 해당 채팅방으로 응답을 전송합니다."
          },
          "markAsRead": {
            "!type": "fn() -> bool|fn(room: string) -> bool|fn(room: string, packageName: string) -> bool",
            "!doc": "해당 채팅방에 응답을 보내지 않고 읽음으로 처리합니다."
          }
      }
    },
    // "someOtherGlobal": "string"
}

const ImageDB = {
    "!name": "imageDB",
    "ImageDB": {
        "!type": "fn()",
        "prototype": {
            "getProfileImage": {
                "!type": "fn() -> string",
                "!doc": "채팅을 보낸 사람의 프로필 사진의 Base64을 반환합니다."
            },
            "getProfileImageBitmap": {
                "!type": "fn() -> android.graphics.Bitmap",
                "!doc": "채팅을 보낸 사람의 프로필 사진의 android.graphics.Bitmap을 반환합니다."
            },
            "getProfileBitmap": {
                "!type": "fn() -> android.graphics.Bitmap",
                "!doc": "채팅을 보낸 사람의 프로필 사진의 android.graphics.Bitmap을 반환합니다."
            },
            "getProfileHash": {
                "!type": "fn() -> string",
                "!doc": "채팅을 보낸 사람의 프로필 사진의 해시코드를 반환합니다."
            },
            "getImage": {
                "!type": "fn() -> string",
                "!doc": "사진이 수신된 경우, 해당 사진의 Base64 반환합니다."
            },
            "getImageBitmap": {
                "!type": "fn() -> android.graphics.Bitmap",
                "!doc": "사진이 수신된 경우, 해당 사진의 android.graphics.Bitmap을 반환합니다."
            }
        },
    }
}