import pandas as pd
import sys
import matplotlib.pyplot as plt
import numpy as np

def load_data():
    if len(sys.argv) < 2:
        print('error')
        sys.exit(1)
    filename = sys.argv[1]
    df = pd.read_csv(filename)
    return df

def plot_data(df):
    plt.figure()
    df['year'] = df['year'].astype(int)
    plt.plot(df['year'], df['days'])
    plt.xticks(df['year'].unique())
    plt.xlabel('Year')
    plt.ylabel('Number of Frozen Days')
    plt.savefig('plot.jpg')

def matrix_x(df):
    df['year'] = df['year'].astype('int64')
    X = np.column_stack((np.ones(df.shape[0], dtype='int64'), df['year'].values))
    return X

def matrix_y(df):
    df['days'] = df['days'].astype('int64')
    Y = df['days'].values
    Y = Y.astype('int64')
    return Y

def transpose_dot(X):
    X_T = X.transpose()
    Z = np.dot(X_T, X)
    Z = Z.astype('int64')
    return Z

def inverse_transpose(Z):
    I = np.linalg.inv(Z)
    return I

#order of I,X matters when doing dot product
def pseudo_inverse(I, X):
    X_T = X.transpose()
    PI = np.dot(I, X_T)
    return PI
    
def mle(Y, PI):
    B = np.dot(PI, Y)
    return B

def prediction(B):
    
    X_test = np.array([[1,2022]])
    Ytest = np.dot(X_test, B)
    return Ytest[0] #[0] is to get rid of array output

def model_interpretation(B):
    B1 = B[1]
    symbol = '>' if B1 > 0 else '<' if B1 < 0 else '='
    
    if symbol == '>':
        answer = "The number of days that are covered with ice is increasing meaning that there will probably be longer winters or colder days."
    elif symbol == '<':
        answer = "The number of days that are covered with ice is decreasing meaning that there will probably be shorter winters or warmer days."
    elif symbol == '=':
        answer = "The number of days that are covered with ice have no change over the years so winter will be stable as it is right now."
    
    return symbol, answer

def predict_model(B):
    if B[1] == 0:
        return np.nan
    x_star = -B[0] / B[1]
    return x_star

def main():
    df = load_data()
    plot_data(df)
    X= matrix_x(df)
    print("Q3a:")
    print(X)
    Y = matrix_y(df)
    print('Q3b:')
    print(Y)
    Z = transpose_dot(X)
    print('Q3c:')
    print(Z)
    I = inverse_transpose(Z)
    print('Q3d:')
    print(I)
    PI = pseudo_inverse(I,X)
    print('Q3e:')
    print(PI)
    B = mle(Y, PI)
    print('Q3f:')
    print(B)
    Ytest = prediction(B)
    print('Q4: ' + str(Ytest))
    symbol, answer = model_interpretation(B)
    print(f"Q5a: {symbol}")
    print(f"Q5b: {answer}")
    x_star = predict_model(B)
    print(f"Q6a: {x_star}")
    print("Q6b: We got a prediction that by 2455 that Lake Mendota will no longer freeze. This can seem true due to climate change, given the trend of winter days slowly decreasing too. I think that the year 2455 is a compelling prediction because it's far out in the future that can match the decreasing process we've been going through over the past decades.")
if __name__ == "__main__":
    main()
