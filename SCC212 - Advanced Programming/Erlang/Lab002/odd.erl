-module(odd).
-export([odd/1]).

%take a list and empty list acc take the head off the list, if this is both integer and odd
%recurse passing in the tail and adding head to the acc list
odd_tr([Head | Tail], Acc) when Head rem 2 == 1 ->
	odd_tr(Tail, [Head | Acc]);

%if the head is not odd (even) recurse passing the tail and the currwent list of even numbers	
odd_tr([_ | Tail], Acc) ->
	odd_tr(Tail,Acc);

%when the list is empty revers the list of even numbers in acc and return
odd_tr(_, Acc) ->
	lists:reverse(Acc).

%wrapper that acepts a list	
odd(List) -> odd_tr(List, []).


	