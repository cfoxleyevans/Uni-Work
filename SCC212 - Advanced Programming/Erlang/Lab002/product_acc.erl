-module(product_acc).
-export([product/1]).

%when the list is empty return sum
product_acc([], Product) -> Product;

%strip the head off the list add it to Prodcut acc then pass the tail recursivley
product_acc([Head | Tail], Product) ->
	product_acc(Tail, Head * Product).

%wrapper function
product(List) -> product_acc(List, 1).