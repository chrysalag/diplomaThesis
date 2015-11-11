import csv
new_rows = []

d = {}

with open('forids.csv', 'r') as f:
        spamreader = csv.reader(f, delimiter=',', quotechar='"')
        i = 0
        for row in spamreader:
                d[row[0]] = i
                i = i + 1

with open('ratings.csv', 'r') as f:
        spamreader = csv.reader(f, delimiter=',', quotechar='"')
        for row in spamreader:
                row[1] = d[row[1]]
                new_rows.append(row) # add the modified rows

with open('ratings.csv', 'w') as f:
        # Overwrite the old file with the modified rows
        writer = csv.writer(f)
        writer.writerows(new_rows)

