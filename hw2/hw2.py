import sys
import math
import random


def get_parameter_vectors():
    '''
    This function parses e.txt and s.txt to get the  26-dimensional multinomial
    parameter vector (characters probabilities of English and Spanish) as
    descibed in section 1.2 of the writeup

    Returns: tuple of vectors e and s
    '''
    #Implementing vectors e,s as lists (arrays) of length 26
    #with p[0] being the probability of 'A' and so on
    e=[0]*26
    s=[0]*26

    with open('e.txt',encoding='utf-8') as f:
        for line in f:
            #strip: removes the newline character
            #split: split the string on space character
            char,prob=line.strip().split(" ")
            #ord('E') gives the ASCII (integer) value of character 'E'
            #we then subtract it from 'A' to give array index
            #This way 'A' gets index 0 and 'Z' gets index 25.
            e[ord(char)-ord('A')]=float(prob)
    f.close()

    with open('s.txt',encoding='utf-8') as f:
        for line in f:
            char,prob=line.strip().split(" ")
            s[ord(char)-ord('A')]=float(prob)
    f.close()

    return (e,s)

def shred(filename):
    #Using a dictionary here. You may change this to any data structure of
    #your choice such as lists (X=[]) etc. for the assignment
    #Q1
    #dic with counts a - z to be 0
    char_count = {chr(i): 0 for i in range(ord('a'), ord('z') + 1)}

    with open (filename,encoding='utf-8') as f:
        # TODO: add your code here
         #counting chars: make lower, filter out non-letter and then add to char_count dic
        content = f.read().lower()
        for char in content:
            if char.isalpha() and char in char_count:
                char_count[char] += 1
    #print("Q1")
    output = ["Q1"]
    for i in range(26):
        #turn into capital
        letter = chr(ord('a') + i).upper()
        count = char_count[chr(ord('a') + i)]
        #formatting output for dict
        output.append(f"{letter} {count}")

    return '\n'.join(output)

#Bayes RUle Purpose: its used as language identifier
#prior probs: P(Y = English) = 0.6
# P(Y = Spanish) = 0.4

#Calculate multinomial prob : P(X|Y = y)
#e = (e_1...e_26)
def multinomial_prob(counts, prob):
    #log to avoid factorials
    log = 0
    for i in range(26):
        #to avoid math error
        if counts[i] > 0:
            log += counts[i] * math.log(prob[i])
    return log

def lang_identifier(char_count, e, s):
    #dict -> list of counts
    counts = [char_count.get(chr(i)) for i in range(ord('a'), ord('z') + 1)]

    #log for each lang
    log_e = multinomial_prob(counts, e) + math.log(0.6)
    log_s = multinomial_prob(counts, s) + math.log(0.4)

    #return higher prob , meaning its prob that lang
    #^modifying to handle underflow/overflow
    if log_e > log_s:
        return "English"
    else:
        return "Spanish"

    

def print_q2(char_count, e, s):
    x1 = char_count.get('a', 0)
    x1_log_e = x1 * math.log(e[0])
    x1_log_s = x1 *math.log(s[0])
    print("Q2")
    print(f"{x1_log_e:.4f}")
    print(f"{x1_log_s:.4f}")

#similar to 2 but this is overall prob log so we just copy from lang_identifier()
def print_q3(char_count, e,s):
    counts = [char_count.get(chr(i)) for i in range(ord('a'), ord('z') + 1)]

    #log for each lang
    log_e = multinomial_prob(counts, e) + math.log(0.6)
    log_s = multinomial_prob(counts, s) + math.log(0.4)

    print("Q3")
    print(f"{log_e:.4f}")
    print(f"{log_s:.4f}")

#print out P(Y= English | X)
def print_q4(char_count, e, s):
    counts = [char_count.get(chr(i)) for i in range(ord('a'), ord('z') + 1)]
    #copy pasting from other funcs
    log_e = multinomial_prob(counts, e) + math.log(0.6)
    log_s = multinomial_prob(counts, s) + math.log(0.4) 


    diff = log_s - log_e
    
    if diff >= 100:
        log_prob_e = 0.0
    elif diff <= -100:
        log_prob_e = 1.0
    else:
        log_prob_e = 1 / (1 + math.exp(diff))


    print("Q4")
    print(f"{log_prob_e:.4f}")


def main():
    e,s = get_parameter_vectors()
    result = shred('letter.txt')
    print(result)
    # Extract character counts from the result of shred
    char_count = {line.split()[0].lower(): int(line.split()[1]) for line in result.split('\n')[1:] if line}
    
    #Q2 
    print_q2(char_count, e, s)

    #Q3
    print_q3(char_count,e,s)

    #Q4
    print_q4(char_count, e,s)
    
if __name__ == "__main__":
    main()








# TODO: add your code here for the assignment
# You are free to implement it as you wish!
# Happy Coding!

