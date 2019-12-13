import random

from telegram import Update
from telegram.ext import Updater
from telegram.ext import CommandHandler
from telegram.ext import MessageHandler
from telegram.ext import Filters
from telegram import ReplyKeyboardMarkup

BUTTON_MAGIC = "Magic"

LINES = []

reply_keyboard = [[BUTTON_MAGIC]]
markup = ReplyKeyboardMarkup(reply_keyboard)

def do_start(update: Update, context):
    print("get start")
    update.message.reply_text('Привет! Этот бот рассказывает о значении слов, которые мало кто знает. Нажми Magic, чтобы узнать новое слово', reply_markup=markup)
    print("send start")

def do_help(update: Update, context):
    print("get help")
    update.message.reply_text("Этот бот рассказывает о значении слов, которые мало кто знает. Нажми Magic, чтобы узнать новое слово")
    print("send help")

def do_message(update: Update, context):
    if update.message.text == BUTTON_MAGIC:
        print("get magic")
        text = random.choice(LINES)
        update.message.reply_text(text)
        print("send magic")
        return
    return do_help(update=update, context=context)

def main():
    TG_TOKEN = "911623437:AAHiIIubApc44c3v0P0I_82Hg6orFzxo96I"
    REQUEST_KWARGS = {
        'proxy_url': 'socks5://166.62.59.87:41970'
        }
    updater = Updater(token = TG_TOKEN, request_kwargs=REQUEST_KWARGS, use_context=True)
    start_handler = CommandHandler("start", do_start)
    help_handler = CommandHandler("help", do_help)
    message_handler = MessageHandler(Filters.text, do_message)
    updater.dispatcher.add_handler(start_handler)
    updater.dispatcher.add_handler(message_handler)
    updater.dispatcher.add_handler(help_handler)

    file = open("words.txt", encoding='utf-8')
    global LINES
    LINES = file.readlines()
    updater.start_polling()

    print("start bot")
    updater.idle()

if __name__ == '__main__':
    main()