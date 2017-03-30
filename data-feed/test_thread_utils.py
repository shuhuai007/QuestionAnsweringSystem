#!/usr/bin/env python
#coding=utf-8
import sys
reload(sys)
sys.setdefaultencoding("utf-8")

from thread_utils import *
def add(i):
    print i

test_list = [1,2,3,4,5]
wm = WorkerManager(2)
for i in test_list:
    wm.add_job(add, i)
wm.wait_for_complete()

