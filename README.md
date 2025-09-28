# 🏀 Sports Stats GraphQL API

A comprehensive, GraphQL API for managing basketball statistics, built with Spring Boot and modern GraphQL best practices.

## 🎯 Overview

This GraphQL API provides a complete solution for managing basketball team statistics, including teams, players, matches, and performance data.

### 🌟 Key Features

- **📊 Complete CRUD Operations** - Full management of teams, players, matches, and statistics
- **🔍 Advanced Querying** - Pagination, filtering, sorting, and search across all entities
- **⚡ Real-time Updates** - WebSocket-based GraphQL subscriptions for live data

### 🏗️ Architecture

- **Backend**: Spring Boot 3.5.6 with Spring GraphQL
- **Database**: H2 In-Memory Database with JPA/Hibernate
- **Real-time**: WebSocket support for GraphQL subscriptions

## 🚀 Quick Start

### Prerequisites

- Java 21 or higher
- Maven 3.6+

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/gokhanaker/sports-stats-api.git
   cd sports-stats-api
   ```

2. **Build the project**

   ```bash
   mvn clean compile
   ```

3. **Run the application**

   ```bash
   mvn spring-boot:run
   ```

4. **Access the API**
   - GraphQL Endpoint: http://localhost:8090/graphql
   - GraphiQL Interface: http://localhost:8090/graphiql
   - H2 Database Console: http://localhost:8090/h2-console

## 📋 API Operations

### 🔍 **Query Operations**

| Category                 | Operations                                               | Description                      |
| ------------------------ | -------------------------------------------------------- | -------------------------------- |
| **Basic Queries**        | `teams`, `players`, `matches`, `team(id)`, `player(id)`  | Simple entity retrieval          |
| **Relationship Queries** | `playersByTeam`, `matchesByTeam`, `playerStats`          | Related data queries             |
| **Search Queries**       | `searchPlayers`, `teamsByCity`, `liveMatches`            | Filtered searches                |
| **Paginated Queries**    | `teamsPaginated`, `playersPaginated`, `matchesPaginated` | Pagination support               |
| **Advanced Filtered**    | `playersFiltered`, `matchesFiltered`, `teamsFiltered`    | Complex multi-criteria filtering |

### ✏️ **Mutation Operations**

| Category              | Operations                                                  | Description                 |
| --------------------- | ----------------------------------------------------------- | --------------------------- |
| **Team Management**   | `createTeam`, `updateTeam`, `deleteTeam`                    | Team CRUD operations        |
| **Player Management** | `createPlayer`, `updatePlayer`, `deletePlayer`              | Player CRUD operations      |
| **Match Management**  | `createMatch`, `updateMatchScore`, `startMatch`, `endMatch` | Match lifecycle management  |
| **Statistics**        | `recordStats`, `updateStats`                                | Player performance tracking |

### 🔴 **Subscription Operations**

| Operation                    | Description                                         |
| ---------------------------- | --------------------------------------------------- |
| `matchScoreUpdate(matchId)`  | Real-time score updates for specific match          |
| `liveMatchUpdates`           | All live match updates across all matches           |
| `matchStatusUpdate(matchId)` | Match status changes (SCHEDULED → LIVE → COMPLETED) |

## 🛡️ Data Model

### Core Entities

- **🏀 Team**: Basic info, relationships to players and matches, computed statistics
- **👤 Player**: Personal info, position, team relationship, performance averages
- **🏟️ Match**: Game details, teams, scores, status, computed results
- **📈 Stats**: Game statistics, shooting percentages, performance metrics

## 🔧 Configuration

### Database Configuration

- **Type**: H2 In-Memory Database
- **Console**: Available at `/h2-console`
- **Credentials**: admin/password
- **Auto-initialization**: Sample data loaded on startup

### GraphQL Configuration

- **Endpoint**: `/graphql`
- **WebSocket**: `/graphql` (for subscriptions)
- **GraphiQL**: `/graphiql` (development interface)
- **Schema**: Auto-generated from resolver methods
