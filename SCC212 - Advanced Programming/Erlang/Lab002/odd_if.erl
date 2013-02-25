-module(odd_if).
-export([odd/1]).

%takes a list strips off the head checks to see if the head is odd/even if it is odd
%takes the head of the list adds it to the acc and recurses over the tail
%if it is even it recurses over the tail
odd_tr_if([Head | Tail], Acc) ->
	if
		Head rem 2 == 1 -> odd_tr_if(Tail, [Head | Acc]);
		Head rem 2 == 0 -> odd_tr_if(Tail, Acc)
	end;

%when the list is empty revers the list of even numbers in acc and return
odd_tr_if(_, Acc) ->
	lists:reverse(Acc).

%wrapper that acepts a list and calls the arity 2 odd_tr_if	
odd(List) -> odd_tr_if(List, []).