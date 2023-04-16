import { Replier } from "./Replier";

type replierType = typeof Replier;


function response(room: string, sender: string, msg: string, replier: replierType) {
    replier.reply(msg);
}