package edu.rx.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.ConnectableObservable;

public class NumberSum {

	static ConnectableObservable<String> from(final InputStream stream){
		return from(new BufferedReader(new InputStreamReader(stream)));
	}
	
	static ConnectableObservable<String> from(final BufferedReader reader){
		OnSubscribe<String> onSubscribe = new MyOnSubscribe(reader);
		return Observable.create(onSubscribe).publish();
	}
	
	static class MyOnSubscribe implements OnSubscribe<String>{
		final BufferedReader reader;
		
		public MyOnSubscribe (final BufferedReader reader){
			this.reader = reader;
		}
		
		public void call(Subscriber<? super String> subscriber) {
			if (subscriber.isUnsubscribed()) return;
			// try reading
			try{
				String line;
				while(!subscriber.isUnsubscribed() && ((line = reader.readLine()) != null)){
					if (line == null || line.equals("exit")) break;
					subscriber.onNext(line); // push to onNext
				}
			}catch(IOException e) {
				subscriber.onError(e); // push to onError
			}
			if (!subscriber.isUnsubscribed()) subscriber.onCompleted(); // push to onCompleted
			
		}
		
	}
	

	public static Observable<Double> varStream(final String varName, Observable<String> input){
		final Pattern pattern = Pattern.compile("\\^s*" + varName + "\\s*[:|=]\\s*(-?\\d+\\.?\\d*)$");
		
		return input.map(
			new Func1 <String, Matcher>(){
				public Matcher call(String str){
					return pattern.matcher(str);
				}
			}
		).filter(
			new Func1<Matcher, Boolean>(){
				public Boolean call(Matcher matcher){
					return matcher.matches() && matcher.group(1) != null;
				}
			}
		).map(
			new Func1<Matcher, Double>(){
				public Double call(Matcher matcher){
					return Double.parseDouble(matcher.group(1));
				}
			}
		);
	}
	
	private static final class ReactiveSum implements Observer<Double>{
		private double sum;
		
		
		public ReactiveSum(Observable<Double> a, Observable<Double> b){
			this.sum = 0;
			Observable.combineLatest(a,b, new Func2<Double, Double, Double>(){
				public Double call(Double a, Double b){
					return a+b;
				}
			
			}
					
					);
		}
		
		public void onCompleted() {
			System.out.println("this is the sum: " + sum);
			
		}

		public void onError(Throwable e) {
			System.out.println(e.getMessage());
			
		}

		public void onNext(Double t) {
			System.out.println("update a + b = " +sum);
		}
		
	}
	
	public void sumInput(){
		
		ConnectableObservable<String> input = from(System.in);
		Observable<Double> a = varStream("a", input);
		Observable<Double> b = varStream("b", input);
		ReactiveSum sum = new ReactiveSum(a,b);
		input.connect();
	}

}
