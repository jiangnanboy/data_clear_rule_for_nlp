                                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                                     chinese data cleaning rules
                                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

##### Guide
对于nlp任务的中文数据清洗规则：
chinese data cleaning rules for nlp tasks

    1）过滤数据是英文的数据 -> bool
    filter data with english title
    
    2）过滤空的数据 -> bool
    filter empty data
    
    3）过滤一个包含少于10个汉字的数据 -> bool
    filter the data that contains less than 10 characters
    
    4）过滤中文字符比例小于50%的数据 -> bool
    filter the data that contains less than 50% Chinese characters
    
    5）中英数字比例过低 -> bool
    the ratio of Chinese, English and numbers is too low
    
    6）统计标点符号比例，过滤掉标点符号占比过低或过多的数据 -> bool
    too much or too little punctuation
    
    7）段落的最后一个token是否标点符号 -> bool
    whether the last token of a paragraph is a punctuation
    
    8）身份证号码、电话号码、电子邮件地址、ip以及url等） -> bool
    whether the content contains ID card number, mobile number, telephone number , email address, url, ip etc
    
    9）从数据中删除异常符号（如表情符号、标志等） -> str
    if a special symbol has no meaning, only characters are deleted
    
    10）删除包含超过十个连续非中文字符的数据 -> str
    for consecutive special symbols, the character is deleted

##### Contact

1、github：https://github.com/jiangnanboy

2、QQ:2229029156

3、Email:2229029156@qq.com


