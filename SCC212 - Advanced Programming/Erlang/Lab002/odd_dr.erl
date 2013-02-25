-module(odd_dr).
-export([odd_dr/1]).



%second clause for when ther is nothing left in the list.
odd_dr([]) -> [].

%take a list strip the head off the list if its odd then hold on to it on the stack frame and 
%recurse over the tail.
%if its even then throw away and recurse over the tail
odd_dr([Head|Tail]) ->
	if 
		Head rem 2 == 1 -> [Head | odd_dr(Tail)];
		Head rem 2 == 0 -> odd_dr(Tail)
	end;