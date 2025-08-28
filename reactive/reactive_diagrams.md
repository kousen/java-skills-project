# Reactive Programming Diagrams

## Diagram 1: Blocking vs Non-blocking I/O

```mermaid
graph TB
    subgraph "Traditional Blocking I/O"
        R1[Request 1] --> T1[Thread 1]
        T1 --> DB1[(Database)]
        DB1 -.->|Thread Blocked<br/>Waiting| T1
        T1 --> RS1[Response 1]
        
        R2[Request 2] --> T2[Thread 2]
        T2 --> DB2[(Database)]
        DB2 -.->|Thread Blocked<br/>Waiting| T2
        T2 --> RS2[Response 2]
        
        R3[Request 3] --> T3[Thread 3]
        T3 --> DB3[(Database)]
        DB3 -.->|Thread Blocked<br/>Waiting| T3
        T3 --> RS3[Response 3]
    end
    
    style T1 fill:#ff9999,stroke:#333,stroke-width:2px,color:#000
    style T2 fill:#ff9999,stroke:#333,stroke-width:2px,color:#000
    style T3 fill:#ff9999,stroke:#333,stroke-width:2px,color:#000
```

```mermaid
graph TB
    subgraph "Reactive Non-blocking I/O"
        R1[Request 1] --> E[Event Loop]
        R2[Request 2] --> E
        R3[Request 3] --> E
        
        E --> DB[(Database)]
        DB -.->|Async Callback| E
        
        E --> RS1[Response 1]
        E --> RS2[Response 2]
        E --> RS3[Response 3]
    end
    
    style E fill:#99ff99,stroke:#333,stroke-width:2px,color:#000
```

## Diagram 2: Mono and Flux Publishers

```mermaid
graph LR
    subgraph "Mono - 0 or 1 Element"
        M[Mono Producer] -->|Single Value| S1[Subscriber]
        M -->|Empty| S2[Subscriber]
        M -->|Error| S3[Subscriber]
    end
```

```mermaid
graph LR
    subgraph "Flux - 0 to N Elements"
        F[Flux Producer] -->|Element 1| S[Subscriber]
        F -->|Element 2| S
        F -->|Element 3| S
        F -->|...| S
        F -->|Complete| S
        F -->|Error| S
    end
```

## Diagram 3: Reactive Stream Flow

```mermaid
sequenceDiagram
    participant S as Subscriber
    participant P as Publisher
    participant D as Data Source
    
    S->>P: subscribe()
    P->>S: onSubscribe(Subscription)
    S->>P: request(n)
    
    loop For each element
        P->>D: fetch data
        D->>P: data
        P->>S: onNext(element)
    end
    
    alt Success
        P->>S: onComplete()
    else Error
        P->>S: onError(throwable)
    end
```

## Diagram 4: Backpressure Strategies

```mermaid
graph TB
    subgraph "Fast Producer, Slow Consumer"
        FP[Fast Producer<br/>1000 items/sec] -->|Data Stream| Buffer
        Buffer -->|Controlled Flow| SC[Slow Consumer<br/>10 items/sec]
        
        Buffer --> Strategy{Backpressure<br/>Strategy}
        Strategy -->|Buffer| B[Store in Memory]
        Strategy -->|Drop| D[Drop Oldest/Latest]
        Strategy -->|Error| E[Throw Exception]
        Strategy -->|Latest| L[Keep Only Latest]
    end
```

## Diagram 5: Operators Pipeline

```mermaid
graph LR
    Source[Source<br/>Flux.just<br/>1,2,3,4,5] -->|Stream| Filter
    Filter[filter<br/>n > 2] -->|3,4,5| Map
    Map[map<br/>n * 2] -->|6,8,10| Take
    Take[take<br/>2] -->|6,8| Subscribe
    Subscribe[subscribe<br/>System.out::println] -->|Output| Console[Console<br/>6<br/>8]
    
    style Source fill:#e1f5fe,stroke:#333,stroke-width:2px,color:#000
    style Subscribe fill:#c8e6c9,stroke:#333,stroke-width:2px,color:#000
```

## Diagram 6: Combining Reactive Streams

```mermaid
graph TB
    subgraph "Zip Operation"
        M1[Mono: Employee] --> ZIP{Zip}
        M2[Mono: Department] --> ZIP
        M3[Mono: Salary] --> ZIP
        ZIP --> Profile[Combined Profile]
    end
    
    subgraph "Merge Operation"
        F1[Flux: Team A] --> MERGE{Merge}
        F2[Flux: Team B] --> MERGE
        MERGE --> All[All Members<br/>Interleaved]
    end
    
    subgraph "Concat Operation"
        F3[Flux: Managers] --> CONCAT{Concat}
        F4[Flux: Engineers] --> CONCAT
        CONCAT --> Ordered[Ordered List<br/>Managers First]
    end
```

## Diagram 7: Error Handling Strategies

```mermaid
flowchart TB
    Start[Reactive Stream] --> Error{Error Occurs?}
    
    Error -->|Yes| Strategy{Error Strategy}
    Error -->|No| Continue[Continue Processing]
    
    Strategy --> Return[onErrorReturn<br/>Default Value]
    Strategy --> Resume[onErrorResume<br/>Alternative Stream]
    Strategy --> Retry[retry<br/>Try Again]
    Strategy --> Map[onErrorMap<br/>Transform Exception]
    
    Return --> Result[Result Stream]
    Resume --> Result
    Retry --> Start
    Map --> Result
    Continue --> Result
```

## Diagram 8: Circuit Breaker Pattern (from Microservices)

```mermaid
stateDiagram-v2
    [*] --> Closed
    Closed --> Open : Failure Threshold Exceeded
    Open --> HalfOpen : After Timeout
    HalfOpen --> Closed : Success
    HalfOpen --> Open : Failure
    
    Closed : Normal Operation
    Closed : All requests pass through
    
    Open : Fast Fail
    Open : Return fallback immediately
    
    HalfOpen : Testing
    HalfOpen : Limited requests to test recovery
```

## Diagram 9: Hot vs Cold Publishers

```mermaid
graph TB
    subgraph "Cold Publisher"
        CP[Cold Publisher] --> S1[Subscriber 1<br/>Gets: 1,2,3]
        CP --> S2[Subscriber 2<br/>Gets: 1,2,3]
        CP --> S3[Subscriber 3<br/>Gets: 1,2,3]
        Note1[Each subscriber gets<br/>their own sequence]
    end
    
    subgraph "Hot Publisher"
        HP[Hot Publisher<br/>Broadcasting: 1,2,3,4,5...] 
        HP -->|...3,4,5| HS1[Subscriber 1<br/>Joined early]
        HP -->|...4,5| HS2[Subscriber 2<br/>Joined late]
        HP -->|...5| HS3[Subscriber 3<br/>Joined later]
        Note2[Subscribers share<br/>the same sequence]
    end
```

## Diagram 10: Reactive Event Stream Architecture

```mermaid
graph TB
    subgraph "Event Sources"
        E1[Employee Created]
        E2[Employee Updated]
        E3[Employee Deleted]
    end
    
    subgraph "Event Sink"
        Sink[Reactive Sink - Sinks.many]
        E1 --> Sink
        E2 --> Sink
        E3 --> Sink
    end
    
    subgraph "Event Stream"
        Sink --> Stream[Flux Event Stream]
    end
    
    subgraph "Subscribers"
        Stream --> Dashboard[Real-time Dashboard]
        Stream --> Audit[Audit Logger]
        Stream --> Analytics[Analytics Engine]
        Stream --> Notification[Notification Service]
    end
    
    style Sink fill:#ffeb3b,stroke:#333,stroke-width:2px,color:#000
    style Stream fill:#4caf50,stroke:#333,stroke-width:2px,color:#000
```

## Usage in Video

These diagrams can be shown during the screen share portion to visualize:
1. **Diagram 1**: When explaining blocking vs non-blocking (Part 1)
2. **Diagram 2**: When introducing Mono and Flux (Part 2)
3. **Diagram 3**: When showing the subscription model
4. **Diagram 5**: When demonstrating operator chaining
5. **Diagram 6**: When showing the combine demo endpoint
6. **Diagram 7**: When demonstrating error handling
7. **Diagram 4**: When explaining backpressure

The diagrams help students visualize the abstract concepts of reactive programming and understand the flow of data through reactive streams.