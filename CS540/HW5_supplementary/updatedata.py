import pandas as pd


df = pd.read_csv('data.csv', usecols=['Winter', 'Days of Ice Cover'])
df.rename(columns={
    'Winter' : 'year',
    'Days of Ice Cover' : 'days'
    }, inplace= True)
df['year'] = df['year'].str.split('-').str[0].astype(int)
df['days'] = pd.to_numeric(df['days'], errors='coerce').fillna(0)

# Aggregate days for years with multiple entries
#this was on piazza on why my csv was wrong
#"Your CSV is not right. Something to start with: 
#make sure you are handling years with multiple freeze-thaw cycles correctly."
df_aggregated = df.groupby('year', as_index=False).agg({'days': 'sum'})
df_aggregated['days'] = df_aggregated['days'].astype(int)

# Ensure the DataFrame is filtered for the years 1855 to 2021
df_filtered = df_aggregated[df_aggregated['year'].between(1855, 2021)]
df_filtered.to_csv('hw5.csv', index=False)

    