# items = []
# items[0] = {
#     'info': {
#         'userName': 'linus',
#         'peroid': 876
#     }
# }
# items[1] = {
#     'info': {
#         'userName': 'linus',
#         'peroid': 876
#     }
# }
vals = {
    'userName': 'linus kupa',
    23: 876
}

books = {
    'white book': '白皮书',
    'red book': '红皮书',
    'yellow book': '黄皮书'
}

# for key, val in vals.items():
#     print(str(val).title())

squares = [val ** 2 for val in range(1, 10)]

nums = squares[:]

# nums = []

squares.append(100)

#for val in range(2, 30):
    # squares.append(val ** 2)
#print(squares)

# if 136 not in nums:
#     print(nums)

for book in sorted(books.keys()):
    print(book.title())    