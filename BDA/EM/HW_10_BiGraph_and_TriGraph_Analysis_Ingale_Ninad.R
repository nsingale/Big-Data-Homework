setwd("E:/BDA Homework/HW 10")
getwd()
words <- read.table("words.txt", quote="\"", comment.char="")
install.packages("stringr")
require(stringr)
words_modified<-str_replace_all(words$V1,"[^[:alnum:]]","")
write.table(words_modified,"words_modified.txt")
words_modified_table<-read.table("words_modified.txt", quote="\"", comment.char="")

str<-words_modified_table[1:99171,1]

find_count<-function(str) if (str[[1]]!=-1) length(str) else 0 


require(sqldf)
##############A###################################
count_nn<-gregexpr("nn+",str,ignore.case = TRUE)
result<-sapply(count_nn,find_count)
data_frame<-data.frame(str,result)
count_ta_df<-sqldf("select sum(result) from data_frame where result>=1")

count_ss<-gregexpr("ss+",str,ignore.case = TRUE)
result<-sapply(count_ss,find_count)
data_frame<-data.frame(str,result)
count_ta_df<-sqldf("select sum(result) from data_frame where result>=1")

##############B&C###################################

count_s<-gregexpr("s$",str,ignore.case = TRUE)
result<-sapply(count_s,find_count)
data_frame<-data.frame(str,result)
count_s_df<-sqldf("select count(*) from data_frame where result>=1")

##############D###################################

count_ta<-gregexpr("ta",str,ignore.case = TRUE)
result<-sapply(count_ta,find_count)
data_frame<-data.frame(str,result)
count_ta_df<-sqldf("select sum(result) from data_frame where result>=1")

count_ta<-gregexpr("te",str,ignore.case = TRUE)
result<-sapply(count_ta,find_count)
data_frame<-data.frame(str,result)
count_ta_df<-sqldf("select sum(result) from data_frame where result>=1")

count_ta<-gregexpr("ti",str,ignore.case = TRUE)
result<-sapply(count_ta,find_count)
data_frame<-data.frame(str,result)
count_ta_df<-sqldf("select sum(result) from data_frame where result>=1")

count_ta<-gregexpr("to",str,ignore.case = TRUE)
result<-sapply(count_ta,find_count)
data_frame<-data.frame(str,result)
count_ta_df<-sqldf("select sum(result) from data_frame where result>=1")

count_ta<-gregexpr("tu",str,ignore.case = TRUE)
result<-sapply(count_ta,find_count)
data_frame<-data.frame(str,result)
count_ta_df<-sqldf("select sum(result) from data_frame where result>=1")

##############F###################################

count_s<-gregexpr("er$",str,ignore.case = TRUE)
result<-sapply(count_s,find_count)
data_frame<-data.frame(str,result)
count_s_df<-sqldf("select sum(result) from data_frame where result>=1")


count_s<-gregexpr("ed$",str,ignore.case = TRUE)
result<-sapply(count_s,find_count)
data_frame<-data.frame(str,result)
count_s_df<-sqldf("select sum(result) from data_frame where result>=1")

##############G###################################

count_s<-gregexpr("ant",str,ignore.case = TRUE)
result<-sapply(count_s,find_count)
data_frame<-data.frame(str,result)
count_s_df<-sqldf("select sum(result) from data_frame where result>=1")


count_s<-gregexpr("ent",str,ignore.case = TRUE)
result<-sapply(count_s,find_count)
data_frame<-data.frame(str,result)
count_s_df<-sqldf("select sum(result) from data_frame where result>=1")

##############J&K###################################

count_s<-gregexpr("tio",str,ignore.case = TRUE)
result<-sapply(count_s,find_count)
data_frame<-data.frame(str,result)
count_s_df<-sqldf("select sum(result) from data_frame where result>=1")

count_s<-gregexpr("ion",str,ignore.case = TRUE)
result<-sapply(count_s,find_count)
data_frame<-data.frame(str,result)
count_s_df<-sqldf("select sum(result) from data_frame where result>=1")

count_s<-gregexpr("ene",str,ignore.case = TRUE)
result<-sapply(count_s,find_count)
data_frame<-data.frame(str,result)
count_s_df<-sqldf("select sum(result) from data_frame where result>=1")

count_s<-gregexpr("ine",str,ignore.case = TRUE)
result<-sapply(count_s,find_count)
data_frame<-data.frame(str,result)
count_s_df<-sqldf("select sum(result) from data_frame where result>=1")
#############################################################################

count_s<-gregexpr("qa",str,ignore.case = TRUE)
result<-sapply(count_s,find_count)
data_frame<-data.frame(str,result)
count_s_df<-sqldf("select sum(result) from data_frame where result>=1")

count_s<-gregexpr("qe",str,ignore.case = TRUE)
result<-sapply(count_s,find_count)
data_frame<-data.frame(str,result)
count_s_df<-sqldf("select sum(result) from data_frame where result>=1")

