package edu.rx.examples;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

public class Iteration {

	private Consumer<Object> printer = System.out::println;
	public List<String> names = Arrays.asList("Trump", "Hilary", "Keshic", "Ted");
	
	/* Regular way of printing names*/
	public void printStrings(){
		printer.accept("-- Regular --");

		Iterator<String> itr = names.iterator();
		while(itr.hasNext()){
			printer.accept(itr.next());
		}
	}
	
	/* @TODO rewrite in a more JAVA8 way */
	public void rx_printStrings(){
		printer.accept("-- RxJava --");
		
		Observable<String> oNames = Observable.from(names);

		oNames.subscribe(
			new Action1<String>(){
				public void call (String element){
					if (element == null) throw new IllegalArgumentException();
					printer.accept(element);
				}
			},
			// on error
			new Action1<Throwable>(){
				public void call(Throwable t){
					printer.accept("-- Exception happened --");
					printer.accept(t);
				}
			},
			// on finish
			new Action0(){
				public void call(){
					printer.accept("--Finished listing--");
				}
			}
		);
	}
}
