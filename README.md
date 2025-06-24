# SafeLanes-v0 Documentation

# Project Overview
- Brief summary of SafeLanes and project goals
- Key features and intended users
# System Architecture
- High-level architecture diagram description (microservices, data flow)
- Main components (data ingestion, processing, APIs, frontend)
- Cloud deployment platform
# Data Sources and Ingestion
- List of external data sources (e.g., crime data, Google Maps API)
- Data ingestion pipeline overview
- Data validation and cleaning approach
# Real-Time Processing
- Overview of Kafka streaming architecture
- Data flow from ingestion to real-time safety scoring
- Message formats and processing logic
# Scoring and Clustering Model
- Clustering approach and algorithms used
- Safety scoring methodology
- Model interpretability and output
# API Design
- Overview of REST API endpoints (FastAPI, Springboot)
- Authentication and access control methods (OAuth2, JWT)
- Key request/response formats
# Data Storage
- Description of storage solutions (PostgreSQL, MongoDB, Redis)
- Data schema and indexing overview
- Data retention and archival strategy
# Security
- User authentication and authorization approach
- Data privacy considerations
# Logging and Monitoring
- Components and operations to be logged
- Monitoring tools and alerting strategies
# Model Retraining and Updates
- Automated retraining pipeline overview
- Model deployment and rollback approach
# Performance and Scalability
- Expected scale and load
- Strategies for scaling components and handling high traffic
# Maintenance and Support
- Routine maintenance tasks
- Support procedures and escalation paths


# Tasks Before Demo of Prototype V0
- [x] H3 Clustering for Crime Hotspots
- [x] Model Training for Coordinate Safety Scores (XGBOOST)
- [ ] Kafka Message Streaming Setup Between SpringBoot and Flask Service
- [ ] GMaps API setup with billing
- [ ] Springboot endpoint creation
- [ ] A* model building and setup for safe path recommendation
- [ ] Setting up the auth server and api gateway
- [ ] Setting up Redis caching for reduced API calls
- [ ] Running the entire application in a Docker environment
- [ ] testing functionalities
# Screenshots of Progress
> **_1. Hotspot Clusters Across City Of Boston_**

![Screenshot 2025-06-25 at 2.54.35 AM.png](https://eraser.imgix.net/workspaces/3FgYgwGWKTds9smuJX7X/7b5Aq6I5FSgyoprGLWNt8lT5n7n1/YAOPXcu_E8B9vgR5E6xhC.png?ixlib=js-3.7.0 "Screenshot 2025-06-25 at 2.54.35 AM.png")

> _**2. XGBoost Safety Scoring Model results for various testcases**_

```
Loading data...

Original Data Statistics:
Severity range before normalization: 1.00 - 4.00
Safety scores after conversion: 2.00 - 8.75

Safety Score Distribution:
safety_score
2.00     11311
4.25     11134
6.50     68491
8.75    207393
Name: count, dtype: int64

Sample of safety scores:
    Latitude  Longitude  cluster_severity  safety_score
0  42.357791 -71.139371                 1          8.75
1  42.306821 -71.060300                 2          6.50
2  42.346589 -71.072429                 1          8.75
3  42.334182 -71.078664                 1          8.75
4  42.275365 -71.090361                 1          8.75

Cluster map statistics:
Number of clusters: 1072
Safety score range: 2.00 - 8.75
Median safety score: 8.75

Creating features:
Total clusters to process: 1072

Processing cluster 1:
H3 index: 892a30646d7ffff
Safety score: 8.75
Number of neighbors found: 6
Neighbor cells: ['892a30646d3ffff', '892a30646c3ffff', '892a30646c7ffff', '892a306468bffff', '892a306469bffff', '892a306716fffff']
Neighbor safety scores: [8.75, 8.75, 8.75, 8.75, 8.75, 8.75]

Processing cluster 2:
H3 index: 892a3066863ffff
Safety score: 6.50
Number of neighbors found: 6
Neighbor cells: ['892a306687bffff', '892a306686bffff', '892a306686fffff', '892a3066867ffff', '892a3066877ffff', '892a3066873ffff']
Neighbor safety scores: [6.5, 8.75, 8.75, 8.75, 8.75, 8.75]

Processing cluster 3:
H3 index: 892a30663abffff
Safety score: 8.75
Number of neighbors found: 6
Neighbor cells: ['892a3066317ffff', '892a3066307ffff', '892a3066333ffff', '892a30663afffff', '892a30663a3ffff', '892a30663bbffff']
Neighbor safety scores: [4.25, 6.5, 8.75, 6.5, 8.75, 8.75]

Processing cluster 4:
H3 index: 892a3066e4bffff
Safety score: 8.75
Number of neighbors found: 6
Neighbor cells: ['892a3064597ffff', '892a3064587ffff', '892a30645b3ffff', '892a3066e4fffff', '892a3066e43ffff', '892a3066e5bffff']
Neighbor safety scores: [6.5, 8.75, 6.5, 8.75, 4.25, 6.5]

Processing cluster 5:
H3 index: 892a3064d47ffff
Safety score: 8.75
Number of neighbors found: 6
Neighbor cells: ['892a3064d43ffff', '892a3064d4fffff', '892a3064d7bffff', '892a3064d73ffff', '892a3064d0bffff', '892a3064d57ffff']
Neighbor safety scores: [6.5, 8.75, 8.75, 8.75, 8.75, 8.75]

Feature Statistics:
            center           n1  ...           n5           n6
count  1072.000000  1072.000000  ...  1072.000000  1072.000000
mean      8.567397     8.569496  ...     8.567397     8.569496
std       0.740292     0.737610  ...     0.740292     0.737610
min       2.000000     2.000000  ...     2.000000     2.000000
25%       8.750000     8.750000  ...     8.750000     8.750000
50%       8.750000     8.750000  ...     8.750000     8.750000
75%       8.750000     8.750000  ...     8.750000     8.750000
max       8.750000     8.750000  ...     8.750000     8.750000

[8 rows x 7 columns]

Training model...

Splitting data and training model...
Training set size: 857
Test set size: 215
Training XGBoost model...

Test Set RMSE: 0.0492

Feature Importances:
  feature  importance
0  center    0.691274
6      n6    0.073455
4      n4    0.070098
3      n3    0.048378
5      n5    0.045116
2      n2    0.040482
1      n1    0.031197

Testing predictions:

==================================================

Predicting for coordinates: 42.290177, -71.128854
H3 Index: 892a3064e0fffff
Exact cluster match found! Score: 8.75
All scores (center + 6 neighbors): [8.75, 8.75, 8.75, 8.75, 8.75, 8.75, np.float64(8.75)]
Final safety score (min of center+neighbors): 9
Prediction Confidence: 100.00%
Location: 42.290177, -71.128854
Final Safety Score (1=least safe, 10=safest): 9
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.358162, -71.059726
H3 Index: 892a3066027ffff
Exact cluster match found! Score: 6.50
All scores (center + 6 neighbors): [6.5, 8.75, 8.75, 2.0, 4.25, 6.5, 2.0]
Final safety score (min of center+neighbors): 2
Prediction Confidence: 100.00%
Location: 42.358162, -71.059726
Final Safety Score (1=least safe, 10=safest): 2
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.352367, -71.06185
H3 Index: 892a3066143ffff
Exact cluster match found! Score: 6.50
All scores (center + 6 neighbors): [6.5, 2.0, 2.0, 4.25, 6.5, 8.75, 4.25]
Final safety score (min of center+neighbors): 2
Prediction Confidence: 100.00%
Location: 42.352367, -71.06185
Final Safety Score (1=least safe, 10=safest): 2
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.33721, -71.073505
H3 Index: 892a3066e53ffff
Exact cluster match found! Score: 6.50
All scores (center + 6 neighbors): [6.5, 8.75, 6.5, 4.25, 6.5, 8.75, 6.5]
Final safety score (min of center+neighbors): 4
Prediction Confidence: 100.00%
Location: 42.33721, -71.073505
Final Safety Score (1=least safe, 10=safest): 4
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.355923, -71.055582
H3 Index: 892a30661cfffff
Exact cluster match found! Score: 8.75
All scores (center + 6 neighbors): [8.75, 6.5, 4.25, 8.75, 8.75, 8.75, 6.5]
Final safety score (min of center+neighbors): 4
Prediction Confidence: 100.00%
Location: 42.355923, -71.055582
Final Safety Score (1=least safe, 10=safest): 4
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.363725, -71.053817
H3 Index: 892a30660afffff
Exact cluster match found! Score: 6.50
All scores (center + 6 neighbors): [6.5, 8.75, 6.5, 2.0, 6.5, 8.75, 8.75]
Final safety score (min of center+neighbors): 2
Prediction Confidence: 100.00%
Location: 42.363725, -71.053817
Final Safety Score (1=least safe, 10=safest): 2
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.338229, -71.0841
H3 Index: 892a3064593ffff
Exact cluster match found! Score: 8.75
All scores (center + 6 neighbors): [8.75, 8.75, 8.75, 8.75, 6.5, 6.5, 8.75]
Final safety score (min of center+neighbors): 6
Prediction Confidence: 100.00%
Location: 42.338229, -71.0841
Final Safety Score (1=least safe, 10=safest): 6
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.311557, -71.053706
H3 Index: 892a306680bffff
Exact cluster match found! Score: 8.75
All scores (center + 6 neighbors): [8.75, 8.75, 8.75, 8.75, 8.75, 8.75, 8.75]
Final safety score (min of center+neighbors): 9
Prediction Confidence: 100.00%
Location: 42.311557, -71.053706
Final Safety Score (1=least safe, 10=safest): 9
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.329608, -71.084318
H3 Index: 892a30645a3ffff
Exact cluster match found! Score: 2.00
All scores (center + 6 neighbors): [2.0, 8.75, 8.75, 8.75, 8.75, 6.5, 6.5]
Final safety score (min of center+neighbors): 2
Prediction Confidence: 100.00%
Location: 42.329608, -71.084318
Final Safety Score (1=least safe, 10=safest): 2
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.333648, -71.076396
H3 Index: 892a3066e43ffff
Exact cluster match found! Score: 4.25
All scores (center + 6 neighbors): [4.25, 6.5, 8.75, 8.75, 8.75, 6.5, 6.5]
Final safety score (min of center+neighbors): 4
Prediction Confidence: 100.00%
Location: 42.333648, -71.076396
Final Safety Score (1=least safe, 10=safest): 4
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.343979, -71.044453
H3 Index: 892a3066133ffff
Exact cluster match found! Score: 8.75
All scores (center + 6 neighbors): [8.75, 8.75, 8.75, 8.75, 8.75, 8.75, 8.75]
Final safety score (min of center+neighbors): 9
Prediction Confidence: 100.00%
Location: 42.343979, -71.044453
Final Safety Score (1=least safe, 10=safest): 9
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.351035, -71.133987
H3 Index: 892a30646bbffff
Exact cluster match found! Score: 6.50
All scores (center + 6 neighbors): [6.5, 8.75, 8.75, 8.75, 8.75, 8.75, 6.5]
Final safety score (min of center+neighbors): 6
Prediction Confidence: 100.00%
Location: 42.351035, -71.133987
Final Safety Score (1=least safe, 10=safest): 6
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.345975, -71.084612
H3 Index: 892a3066377ffff
Exact cluster match found! Score: 6.50
All scores (center + 6 neighbors): [6.5, 6.5, 8.75, 8.75, 8.75, 8.75, 6.5]
Final safety score (min of center+neighbors): 6
Prediction Confidence: 100.00%
Location: 42.345975, -71.084612
Final Safety Score (1=least safe, 10=safest): 6
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.359581, -71.05081
H3 Index: 892a30661c3ffff
Exact cluster match found! Score: 6.50
All scores (center + 6 neighbors): [6.5, 6.5, 6.5, 8.75, 8.75, np.float64(8.75), 8.75]
Final safety score (min of center+neighbors): 6
Prediction Confidence: 100.00%
Location: 42.359581, -71.05081
Final Safety Score (1=least safe, 10=safest): 6
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.371859, -71.039602
H3 Index: 892a3066557ffff
Exact cluster match found! Score: 6.50
All scores (center + 6 neighbors): [6.5, 8.75, 8.75, 8.75, 8.75, 8.75, 8.75]
Final safety score (min of center+neighbors): 6
Prediction Confidence: 100.00%
Location: 42.371859, -71.039602
Final Safety Score (1=least safe, 10=safest): 6
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.360001, -71.058559
H3 Index: 892a3066037ffff
Exact cluster match found! Score: 2.00
All scores (center + 6 neighbors): [2.0, 6.5, 8.75, 6.5, 6.5, 6.5, 6.5]
Final safety score (min of center+neighbors): 2
Prediction Confidence: 100.00%
Location: 42.360001, -71.058559
Final Safety Score (1=least safe, 10=safest): 2
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.377223, -71.056916
H3 Index: 892a306609bffff
Exact cluster match found! Score: 6.50
All scores (center + 6 neighbors): [6.5, 8.75, 8.75, 8.75, 8.75, 8.75, 8.75]
Final safety score (min of center+neighbors): 6
Prediction Confidence: 100.00%
Location: 42.377223, -71.056916
Final Safety Score (1=least safe, 10=safest): 6
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.346641, -71.045912
H3 Index: 892a3066107ffff
Exact cluster match found! Score: 8.75
All scores (center + 6 neighbors): [8.75, 8.75, 8.75, 8.75, 8.75, 8.75, 8.75]
Final safety score (min of center+neighbors): 9
Prediction Confidence: 100.00%
Location: 42.346641, -71.045912
Final Safety Score (1=least safe, 10=safest): 9
Prediction Confidence: 100.00%

==================================================

Predicting for coordinates: 42.636854, -69.926293
H3 Index: 892a30b6593ffff
No exact cluster match found. Using median score: 8.75
All scores (center + 6 neighbors): [np.float64(8.75), np.float64(8.75), np.float64(8.75), np.float64(8.75), np.float64(8.75), np.float64(8.75), np.float64(8.75)]
Final safety score (min of center+neighbors): 9
Prediction Confidence: 100.00%
Location: 42.636854, -69.926293
Final Safety Score (1=least safe, 10=safest): 9
Prediction Confidence: 100.00%

Process finished with exit code 0
```


