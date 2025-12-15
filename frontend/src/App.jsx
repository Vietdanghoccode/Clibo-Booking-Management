import React, { useState } from 'react';
import AuthForm from './components/AuthForm';
import BookingForm from './components/BookingForm';
import DoctorDashboard from './components/DoctorDashboard';

function App() {
  const [user, setUser] = useState(null);

  // Dữ liệu mẫu ban đầu
  const [appointments, setAppointments] = useState([
    {
      id: 1,
      patient: 'Nguyễn Văn Bệnh Nhân', // Trùng với tên mặc định của patient@gmail.com
      patientEmail: 'patient@gmail.com',
      age: 30, gender: 'Nam',
      time: '08:00', date: '2025-10-20',
      reason: 'Đau đầu, chóng mặt',
      status: 'PENDING',
      tests: [], diagnosis: ''
    },
    {
      id: 2,
      patient: 'Trần Thị B',
      patientEmail: 'b@gmail.com',
      age: 45, gender: 'Nữ',
      time: '09:30', date: '2025-10-20',
      reason: 'Đau ngực trái',
      status: 'PENDING',
      tests: [], diagnosis: ''
    },
  ]);

  // 1. Hàm tạo lịch hẹn mới (Dành cho BookingForm)
  const handleAddAppointment = (newApptData) => {
    const newId = appointments.length > 0 ? Math.max(...appointments.map(a => a.id)) + 1 : 1;
    const newAppointment = {
      id: newId,
      ...newApptData,
      status: 'PENDING',
      tests: [],
      diagnosis: ''
    };
    setAppointments([...appointments, newAppointment]);
  };

  // 2. Hàm hủy lịch hẹn (Dành cho BookingForm)
  const handleCancelAppointment = (id) => {
    if (window.confirm("Bạn có chắc chắn muốn hủy lịch hẹn này không?")) {
      const updatedList = appointments.map(app =>
        app.id === id ? { ...app, status: 'CANCELLED' } : app
      );
      setAppointments(updatedList);
    }
  };

  // 3. Hàm cập nhật trạng thái từ Bác sĩ (Giữ nguyên)
  const handleUpdateAppointment = (updatedApp) => {
    const newList = appointments.map(app =>
      app.id === updatedApp.id ? updatedApp : app
    );
    setAppointments(newList);
  };

  const handleLoginSuccess = (userData) => { setUser(userData); };
  const handleLogout = () => { setUser(null); };

  const renderContent = () => {
    if (!user) return <AuthForm onLoginSuccess={handleLoginSuccess} />;

    if (user.role === 'DOCTOR') {
      return (
        <DoctorDashboard
          user={user}
          appointments={appointments}
          onUpdateAppointment={handleUpdateAppointment}
          onLogout={handleLogout}
        />
      );
    }

    // Truyền thêm props cho BookingForm
    return (
      <BookingForm
        user={user}
        onLogout={handleLogout}
        appointments={appointments}          // Danh sách để xem lịch sử
        onAddAppointment={handleAddAppointment} // Hàm đặt lịch
        onCancelAppointment={handleCancelAppointment} // Hàm hủy lịch
      />
    );
  };

  return <div>{renderContent()}</div>;
}

export default App;