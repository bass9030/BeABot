const ReplierStr = `export declare module Replier {
    
    /**
     * 채팅이 수신된 채팅방으로 응답 전송 
     * @param {string} msg 
     * @returns {boolean}
     */
    function reply(msg: string): boolean;

    /**
     * 해당 채팅방으로 응답 전송 
     * @param {string} room 
     * @param {string} msg 
     * @returns {boolean}
     */
    function reply(room: string, msg: string): boolean;

    /**
     * 해당 메신저 앱의 해당 채팅방으로 응답 전송
     * @param {string} room 
     * @param {string} msg 
     * @param {string} packageName 
     * @returns {boolean}
     */
    function reply(room: string, msg: string, packageName: string): boolean;

    /**
     * ms 밀리초 뒤에 채팅이 수신된 채팅방으로 응답 전송 
     * @param {string} msg 
     * @param {number} ms 
     * @returns {boolean}
     */
    function replyDelayed(msg: string, ms: string): boolean;

    /**
     * ms 밀리초 뒤에 해당 채팅방으로 응답 전송 
     * @param {string} room 
     * @param {string} msg 
     * @param {boolean} ms 
     * @returns 
     */
    function replyDelayed(room: string, msg: string, ms: number): boolean;

    /**
     * 채팅이 수신된 채팅방에 응답을 보내지 않고 읽음으로 처리
     * @returns {boolean}
     */
    function markAsRead(): boolean

    /**
     * 해당 채팅방에 응답을 보내지 않고 읽음으로 처리
     * @param {string} room 
     * @returns {boolean}
     */
    function markAsRead(room: string): boolean;

    /**
     * 해당 앱의 해당 채팅방에 응답을 보내지 않고 읽음으로 처리
     * @param {string} room 
     * @param {string} packageName 
     * @returns {boolean}
     */
    function markAsRead(room: string, packageName: string): boolean;
}

declare global {
    var replierType: typeof Replier;
}`;