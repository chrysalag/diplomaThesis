import csv
new_rows = []

with open('genres.csv', 'r') as f:
        spamreader = csv.reader(f, delimiter=',', quotechar='"')
        i = 0
        for row in spamreader:
                row[0] = i
                new_rows.append(row) # add the modified rows
                i = i + 1

with open('genres.csv', 'w') as f:
        # Overwrite the old file with the modified rows
        writer = csv.writer(f)
        writer.writerows(new_rows)


newRows = []
with open('movies.csv', 'r') as f:
        spamreader = csv.reader(f, delimiter=',', quotechar='"')
        i = 0
        for row in spamreader:
                row[0] = i
                newRows.append(row) # add the modified rows
                i = i + 1

with open('movies.csv', 'w') as f:
        # Overwrite the old file with the modified rows
        writer = csv.writer(f)
        writer.writerows(newRows)

