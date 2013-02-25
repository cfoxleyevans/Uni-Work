-module(product).
-export([product/1]).

%when the list is empty return 1
product([]) -> 1;

%strip the head off the list times it by the tail of the list.
product([Head | Tail]) ->
	Head * product(Tail).
	
	

