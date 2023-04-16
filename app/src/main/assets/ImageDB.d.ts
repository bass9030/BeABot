export module ImageDB {
    /**
     * 채팅을 보낸 사람의 프로필 사진을 Base64로 인코딩된 문자열로 가지고 옴
     * @returns {string}
     */
    function getProfileImage(): string;

    /**
     * 채팅을 보낸 사람의 프로필 사진을 android.graphics.Bitmap 인스턴스로 가지고 옴
     * @returns {android.graphics.Bitmap}
     */
    function getProfileImageBitmap(): android.graphics.Bitmap;

    /**
     * imageDB.getProfileImageBitmap();과 동일
     * @returns {android.graphics.Bitmap}
     */
    function getProfileBitmap(): android.graphics.Bitmap;

    /**
     * 채팅을 보낸 사람의 프로필 사진을 Base64로 인코딩된 문자열에 java.lang.String.hashCode(); 메서드를 실행한 결과를 가지고 옴
     * @returns {string}
     */
    function getProfileHash(): string;

    /**
     * @deprecated
     * 사진이 수신된 경우, 해당 사진을 Base64로 인코딩된 문자열로 가지고 옴
     * (더미함수)
     * @returns {string}
     */
    function getImage(): string;

    /**
     * @deprecated
     * 사진이 수신된 경우, 해당 사진을 android.graphics.Bitmap 인스턴스로 가지고 옴
     * (더미함수)
     * @returns {android.graphics.Bitmap}
     */
    function getImageBitmap(): android.graphics.Bitmap
}

/**
 * Dummy module
 */
export module android {
    /**
     * Dummy namespace
     */
    namespace graphics {
        /**
         * Dummy class
         */
        class Bitmap {
        }
    }
}

declare global {
    var imageDBType: typeof ImageDB;
}