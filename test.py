import random
import time
from datetime import datetime

import pyautogui


def get_now_position():
    # 方法1：获取当前坐标
    x, y = pyautogui.position()
    print(f"当前鼠标坐标: ({x}, {y})")

    # 方法2：实时获取（持续监听）
    try:
        while True:
            x, y = pyautogui.position()
            # 格式化输出（\r 表示在同一行更新）
            print(f"\r鼠标坐标: X={x:4d}, Y={y:4d}", end='')
    except KeyboardInterrupt:
        print("\n程序结束")


def random_click():
    """
    随机时间点击（1-5秒后点击）

    Args:
        x, y: 点击坐标，None表示当前位置
    """
    # 生成1-5秒的随机等待时间
    wait_time = random.uniform(1, 5)
    print(f"等待 {wait_time:.2f} 秒后点击...")

    # 等待随机时间 这个副本的时间

    time.sleep(wait_time+30)

    #第二次点击时间
    wait_second_time = random.uniform(1, 5)
    time.sleep(wait_second_time+5)

    return wait_time






if __name__ == '__main__':
    # get_now_position()
    # pyautogui.click()  # 单击左键
    # 执行点击
    for i in range(10):
        random_click()
        print(f"已在当前位置点击第${i}次，${datetime.now()}")
        pyautogui.click()

