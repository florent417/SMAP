package SMAP.au523923Flow.assignment2.wordlearnerapp.utils;

// Inspired by: (see comment from TommySM)
// https://stackoverflow.com/questions/28172496/android-volley-how-to-isolate-requests-in-another-class
public interface OWLBOTResponseListener<T> {
    void getResult(T object);
}
