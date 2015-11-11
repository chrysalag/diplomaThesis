import csv
new_rows = []
changes = {
        'Action' : 524288,
        'Adventure' : 262144,
        'Animation' : 131072,
        'Children' : 65536,
        'Comedy'   : 32768,
        'Crime' : 16384,
        'Documentary' : 8192,
        'Drama' : 4096,
        'Fantasy' : 2048,
        'Film-Noir' : 1024,
        'Horror' : 512,
        'Musical' : 256,
        'Mystery' : 128,
        'Romance' : 64,
        'Sci-Fi' : 32,
        'Thriller' : 16,
        'War' : 8,
        'Western' : 4,
        'IMAX' : 2,
        '(no genres listed)' : 1
        }

with open('genres.csv', 'r') as f:
        spamreader = csv.reader(f, delimiter=',', quotechar='"')
        for row in spamreader:
                deck =  [changes[x] for x in row[-1].split("|")]
                s = sum(deck)
                s = '{0:20b}'.format(s)
                row[-1] = "|".join(s)
                row[-1] = row[-1].replace(' ','0')
                new_rows.append(row) # add the modified rows

with open('genres.csv', 'w') as f:
        # Overwrite the old file with the modified rows
        writer = csv.writer(f)
        writer.writerows(new_rows)
