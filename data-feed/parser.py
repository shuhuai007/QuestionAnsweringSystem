#!/usr/bin/env python  
#coding=utf-8  
import sys 
reload(sys) 
sys.setdefaultencoding("utf-8")
  
from thread_utils import *
from collections import OrderedDict  
  
from pyexcel_xls import get_data  
from pyexcel_xls import save_data  
  
QUESTION_URL_BASE="http://127.0.0.1:8080/deep-qa-web-1.2/api/ask?n={}&q={}"  

def read_xls_file():  
    xls_data = get_data(r"CorpusWordlist.xls")  
    print "Get data type:", type(xls_data)  

    for sheet_n in xls_data.keys():  
        #print sheet_n, ":", xls_data[sheet_n]  
        #print sheet_n, ":", type(xls_data[sheet_n]) 
        #print sheet_n, ":", len(xls_data[sheet_n]) 
        #print sheet_n, ":", xls_data[sheet_n][7]
        filter_list = list(filter((lambda each_line: len(each_line) > 2),xls_data[sheet_n]))
        map_list = map((lambda line: line[1]),filter_list)

        component_list = []
        for question in map_list:
            print ("question len is %d" % len(question))
            if len(question) > 2:
                from time import sleep
                #sleep(0.2)
                #sendRequest(question)
                #sys.exit(0)
            else:
                component_list.append(question) 
        #print map_list
        combination_list = getCombination(component_list)
        for q in combination_list:
             sendRequest(q)

def getCombination(combination_list, filter_size=3, combination_factor=3):
    print "combination list"
    a = [] 
    for idx, val in enumerate(combination_list):
        if len(val.lstrip().rstrip().decode('utf-8')) > filter_size:
            print "val:{}, len:{}".format(val, len(val.lstrip().rstrip().decode('utf-8')))
            a.append(val)
            continue
        print(idx, val)
        if idx < combination_factor:
            continue
        #new_question = combination_list[idx-3] + combination_list[idx-2] + combination_list[idx-1] + val
        new_question = ""
        for i in range(1, combination_factor):
            import random
            random_idx = random.randint(0, idx - i)
            new_question = combination_list[random_idx] + new_question
        new_question = new_question + val
        
        import re
        new_question = re.sub('[\s+]', '', new_question)
        print "new question:{}".format(new_question)
        a.append(new_question) 
    return a
    
def sendRequest(question):
    print "send request" 
    #get_url = QUESTION_URL_BASE.format(10, question)
    #print get_url 
    #import requests
    #r = requests.get(get_url)
    #r = requests.post(get_url)
    #print r.content

    import requests
    url = 'http://127.0.0.1:8080/deep-qa-web-1.2/api/ask'
    payload = {'n': '10', 'q': question}
    r = requests.post(url, data=payload)
    print "question is: {}".format(question)
    print r.text

def readFile(filePath):
    print "filePath:{}".format(filePath)
    with open(filePath) as f:
        content = f.readlines()
    #print type(content)
    return content


if __name__ == '__main__':  
    #read_xls_file()
    #question = "good"
    #sendRequest(question)
    #c = ["a", "b", "c", "d"]
    #print getCombination(c)

    #filePath = "question.txt" 
    filePath = "dict.txt" 
    content = readFile(filePath)
    questionList = getCombination(content, 3, 3) 
    
    #thread_count = 1
    #wm = WorkerManager(thread_count)
    #for q in questionList:
    #    wm.add_job(sendRequest, q)
    #wm.wait_for_complete()
    map(sendRequest, questionList)
    print questionList

