# Centralized Employee Insights Platform

A comprehensive platform for managing employee data, performance metrics, and analytics across an organization.

## 🚀 Features

- **User Management**: Secure authentication and authorization
- **Performance Tracking**: Monitor and analyze employee performance
- **Analytics Dashboard**: Real-time insights and reporting
- **Notification System**: Stay updated with important alerts
- **API Gateway**: Centralized API management
- **Microservices Architecture**: Scalable and maintainable design

## 🛠️ Tech Stack

### Backend
- Java 17
- Spring Boot 3.x
- Spring Cloud Gateway
- Spring Security with JWT
- Redis for caching
- Kafka for event streaming

### Frontend
- React with TypeScript
- Vite
- TailwindCSS

### Infrastructure
- Docker & Docker Compose
- Prometheus & Grafana
- MySQL
- Kafka
- Redis

## 🔄 API Reference

### User Service (`/api/users`)
| Endpoint | Method | Description | Requires Auth |
|----------|--------|-------------|---------------|
| `/register` | POST | Register a new user | No |
| `/login` | POST | Authenticate user | No |
| `/` | GET | Get all users | Yes (ADMIN) |
| `/{id}` | GET | Get user by ID | Yes |
| `/{id}` | PUT | Update user | Yes (ADMIN or self) |
| `/{id}` | DELETE | Delete user | Yes (ADMIN) |
| `/department/{department}` | GET | Get users by department | Yes |

### Performance Service (`/api/performance`)
| Endpoint | Method | Description | Requires Auth |
|----------|--------|-------------|---------------|
| `/records` | GET | Get all performance records | Yes |
| `/user/{userId}` | GET | Get records by user | Yes |
| `/` | POST | Add performance record | Yes (MANAGER, ADMIN) |
| `/{id}` | PUT | Update record | Yes (MANAGER, ADMIN) |
| `/{id}` | DELETE | Delete record | Yes (ADMIN) |

### Analytics Service (`/api/analytics`)
| Endpoint | Method | Description | Requires Auth |
|----------|--------|-------------|---------------|
| `/metrics` | GET | Get all analytics metrics | Yes |
| `/departments` | GET | Get department stats | Yes |
| `/performance/trends` | GET | Get performance trends | Yes |
| `/engagement` | GET | Get engagement metrics | Yes |

### Notification Service (`/api/notifications`)
| Endpoint | Method | Description | Requires Auth |
|----------|--------|-------------|---------------|
| `/` | GET | Get all notifications | Yes |
| `/unread` | GET | Get unread notifications | Yes |
| `/{id}/read` | PUT | Mark as read | Yes |
| `/` | POST | Create notification | Yes (ADMIN, SYSTEM) |

### Authentication
All authenticated endpoints require a valid JWT token in the Authorization header:
