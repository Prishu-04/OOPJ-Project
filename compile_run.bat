@echo off
REM ============================================================
REM  Hotel Reservation System — Manual compile & run (Windows)
REM  Requires: sqlite-jdbc-3.45.1.0.jar in the libs\ folder
REM ============================================================

SET SRC=src
SET BIN=bin
SET LIB=libs\sqlite-jdbc-3.45.1.0.jar
SET MAIN=main.Main

REM Create output folder
if not exist %BIN% mkdir %BIN%

echo Compiling...
javac -cp "%LIB%" -sourcepath %SRC% -d %BIN% ^
    %SRC%\db\DBConnection.java ^
    %SRC%\model\User.java ^
    %SRC%\model\Admin.java ^
    %SRC%\model\Customer.java ^
    %SRC%\model\Room.java ^
    %SRC%\model\Booking.java ^
    %SRC%\model\Payment.java ^
    %SRC%\dao\UserDAO.java ^
    %SRC%\dao\RoomDAO.java ^
    %SRC%\dao\BookingDAO.java ^
    %SRC%\dao\PaymentDAO.java ^
    %SRC%\view\LoginForm.java ^
    %SRC%\view\RegisterForm.java ^
    %SRC%\view\AdminDashboard.java ^
    %SRC%\view\UserDashboard.java ^
    %SRC%\view\AddRoomForm.java ^
    %SRC%\view\ManageRoomsForm.java ^
    %SRC%\view\ViewRoomsForm.java ^
    %SRC%\view\BookingForm.java ^
    %SRC%\view\MyBookingsForm.java ^
    %SRC%\view\CancelBookingForm.java ^
    %SRC%\view\PaymentForm.java ^
    %SRC%\view\ViewCustomersForm.java ^
    %SRC%\main\Main.java

IF ERRORLEVEL 1 (
    echo Compilation FAILED. Check errors above.
    pause
    exit /b 1
)

echo Compilation successful!
echo Running application...
java -cp "%BIN%;%LIB%" %MAIN%
pause
