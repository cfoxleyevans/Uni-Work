-module(myqueue).
-export([create/0,append/2,get/1,isEmpty/1]).

create() -> [].

append(List, X) -> (List ++[X]).

get([Head|Tail]) -> {Head,Tail}.
 
isEmpty(List) ->
				if List == [] -> true;
                true -> false
                end.
